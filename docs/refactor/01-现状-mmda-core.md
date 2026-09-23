# 01 现状：mmda-core 架构分析

> 分析对象：`D:\2026\java`（MMDA 服务端 Java 重写版，第三代）
> 分析时间：2026-09-24。§1–§5 来自读源码与文件统计（未改动任何源码）；§7 是当天实测跑通的构建与测试基线。
> 配套图：`diagrams/mmda-core-current.architecture.html`（源：`diagrams/mmda-core-current.architecture.json`）

## 1. 坐标与可验证条件

| 项 | 事实 | 证据 |
|---|---|---|
| 是什么 | MMDA 元模型驱动架构的服务端 Java 实现 | `IDEA.md` |
| 构建 | **无根 pom**；11 个顶层目录各是独立 Maven 聚合工程 | 各目录 `pom.xml` |
| VCS | **没有**（无 `.git` / `.svn`，模块内只剩 `svn.log` 残迹） | `ls -a D:\2026\java` |
| 栈 | Java 21 / Spring Boot 3.3.1，版本 `5.0.0` | `mmda-core/pom.xml:31-38` |
| 本机能力 | `mvn` 不在 PATH；PATH 上 java 是 17（工程要 21）；本机另有 JDK 21（`C:\Program Files\Java\jdk-21`）与 IDEA 2026.2.3 自带 Maven 3.9.16 | `mvn -v`、`java -version` |
| 可用构建器（实测） | **JDK 21 + IDEA 2026.2.3 自带 Maven 3.9.16**，已封装备用脚本 `~/AppData/Local/hermes/bin/mmda-mvn.sh` | 见 §7 |
| 最后成功编译 | 各模块 `target/classes` 最新 `2026-07-06 15:03`，`EntityFactory.class`（76 KB）在内 | `ls mmda-core/mmda-core-entities/target/classes/cloud/mmda/core/entities/` |

复算命令：

```bash
# 模块规模（文件数 / 行数）
for m in mmda-*; do \
  n=$(find $m -path '*src/main/java*' -name '*.java' -not -path '*/target/*' | wc -l); \
  l=$(find $m -path '*src/main/java*' -name '*.java' -not -path '*/target/*' -print0 | xargs -0 cat | wc -l); \
  echo "$n files  $l lines  $m"; done
```

模块规模（src/main，实测）：core 483 文件 / 125,039 行；base 365 / 46,413；foundation 119 / 16,745；业务 7 模块共 2,179 文件 / 323,421 行（mes 782 / 140,649 最大）。`mmda/.generated` 106 个 java（以 iot 为主）；`mmda-lang`、`mmda-core-reporting` 是空壳（0 个 java）。

## 2. mmda-core 模块依赖（实测 pom）

```
utils ← metadata ← sql ← entities          entities → {metadata, sql, utils}
                    ↑
       {caching, data} → {entities, metadata, utils}
       services → {data, entities, metadata, caching, security, file, messaging}
       api      → {services, security, data, entities, metadata, caching, utils}
```

两条关键事实：

1. **core-sql 只有 entities 一个真依赖**（另有 `mmda-foundation/mmda-coder`、`mmda-foundation/mmda-factory` 两个生成器 pom）；
2. **core-data 不依赖 core-sql** —— 新 SQL 层与被依赖链是断开的。

## 3. 两套数据访问栈并存

| | 旧栈（主干，业务在用） | 新栈（本轮新加） |
|---|---|---|
| SQL 构件 | `mmda-core/mmda-core-data/src/main/java/cloud/mmda/core/data/sql/`：`SqlQuery.java` 1350 行、`SqlExpression.java` 799 行、`SqlBuilder`、`SqlDateTime`×5、`SqlOperator` | `mmda-core/mmda-core-sql/`：`SqlQuery.java` 425 行、`SqlQueryable`、`SqlCmd` + `SqlExecutable`、`dialects/` 8 方言（2113 行）、`expressions/`、`schema/`、ANTLR4 生成 12 类 |
| 数据访问 | `mmda-core-data/.../data/jdbc/repository/`：`EntityRepository.java` 2010 行、`TenancyEntityRepository` 1051、`Repository` 583、`TenancyRepository` 473 | `mmda-core-entities/.../EntityFactory.java` **2178 行**、`EntityClassAccess.java` 211 行 |
| 元数据 | `mmda-core-data/.../data/jdbc/metadata/`：`SqlMetadataProvider` + 4 方言子类 + `DbMetadataProvider`(310) | 接口在 `mmda-core-metadata/.../metadata/MetadataProvider.java`，经 `SqlDialect.getMetadataProvider()` 取 |
| 引用面（实测） | `core.data.sql.*` 在 base/mes/los/fin 抽查 **250 个文件** | `core.sql.*` 全仓 **4 个文件**（2 个是测试） |
| 迁移面 | 8 个业务模块 **426 个 Repository 子类 / 431 个 Service 子类** | — |

`EntityFactory` 的全部引用点（6 处）：`mmda-base/mmda-base-repository/src/main/java/cloud/mmda/base/BaseEntityFactory.java:13`、`mmda-base/mmda-base-services/src/main/java/cloud/mmda/base/services/UserService2.java`、`mmda-base/mmda-base-repository/src/main/java/cloud/mmda/base/data/PartnerPredicate.java`、测试 2 处（`EntityFactoryTest`、`SqlTableCreator`），以及 **`mmda-core/mmda-core-data/src/main/java/cloud/mmda/core/data/jdbc/repository/EntityRepository.java:75` 一个声明后从未使用的 `protected EntityFactory entityFactory;`**——接线动了半截的现场。

复算命令：

```bash
grep -rln --include='*.java' 'cloud.mmda.core.sql.' mmda-base mmda-mes mmda-los mmda-fin | grep -v '/target/'
grep -rln --include='*.java' 'cloud.mmda.core.data.sql\.' mmda-base mmda-mes mmda-los mmda-fin | grep -v '/target/' | wc -l
grep -rn --include='*.java' 'entityFactory' mmda-core/mmda-core-data/src/main/java/cloud/mmda/core/data/jdbc/repository/EntityRepository.java
```

## 4. EntityFactory 解剖

**成立的部分（值得沿用）**：单一路径 `DataSource → SqlDialect(含 MetadataProvider) → EntityFactory → EntityClassAccess → JdbcTemplate`；`EntityClassAccess` 用 `LazySingletonSupplier` 缓存 `MetaObjectAccess`/`RowMapper`/主键排序，并集中三级命名缓存（`sqlCriteria`/`sqlQueryables`/`sqlCommands`）；命令与查询分离（`SqlCmd`/`SqlQueryable` 都实现 `SqlExecutable`），租户隔离走「编译期进 SQL、执行期按 `MIN_ID`/`MAX_ID` 占位符填值」（`mmda-core-sql/.../SqlExecutable.java:39-115`）。

**不成立的部分**：2178 行 / 151 处 public 成员里塞了 4 个 static 全局注册表、SQL 执行、查询 DSL、命令构建器、`EntitySet`、懒加载关联、租户解析七种职责，且内嵌 8 个 public 类型。

## 5. 问题清单（带行号）

| # | 问题 | 位置 | 后果 |
|---|---|---|---|
| 1 | 注册表是 `static`，方言/元数据/JdbcTemplate 是实例的 | `mmda-core/mmda-core-entities/src/main/java/cloud/mmda/core/entities/EntityFactory.java:60-64` | 多数据源（MySQL/Oracle/SQLServer/TDengine 并存）或同进程多租户互相污染 |
| 2 | `getMetaCol` 是 `static`，却依赖实例注册结果，未 `supply` 即 NPE | 同上 `:229-236` | 静态入口与实例生命周期不一致 |
| 3 | `updatePartialBy` 把 `tenantId` 交给 `getTenantId(Object)`：`int` 装箱成 `Integer` 后一律返回 0 | 同上 `:843` 与 `:616-624`（对比 `deleteBy` `:608` 直接传 `tenantId`） | 分区表 UPDATE 的 min/max 用 0 计算，租户隔离失效（**待运行验证**） |
| 4 | `EntitySet.deleteAll()` 传 `null` 给 `deleteBy`，后者首行 `requireNonNull` | 同上 `:1985-1987` → `:599` | 必抛 NPE |
| 5 | 2 参 `createEagerRowMapper` 传不可变 `List.of()`，函数体却 `relations.addAll(...)` | 同上 `:163-165`、`:141-143` | 死代码 + 必抛 `UnsupportedOperationException`；另有 `createEagerRowMapper(EntityClassAccess)`（`:1375`）无调用者 |
| 6 | `getRelativeRowMapper` 用 `entityRegistries.get(...)`，与 `access()` 的按需注册策略不一致；`HAS_MANY` 分支只取了个没人用的局部变量 | 同上 `:1265-1272` | 关联实体未 `supply` 即 NPE；分支空转 |
| 7 | `rowMappers` 静态字段声明后从未使用；`query(Class, withOne, name)` 的 `name` 参数被忽略 | 同上 `:64`、`:1456-1462` | 死字段 / 死参数 |
| 8 | JDBC 执行器（`JdbcTemplate`/`PreparedStatement`/`RowMapper`）落在模型模块 `mmda-core-entities`，靠 `core-sql` 传递引入 spring-jdbc | `mmda-core-entities/pom.xml` | 模块边界错位；反向脏点在 `EntityRepository.java:75` |
| 9 | 重复面：`whereById(Class)` vs `whereById(EntityClassAccess)`（`:346-351`）、4 个 `access(...)` 重载（`:168-194`）、两套默认行映射（`EntityClassAccess.createDefaultRowMapper` vs `EntityFactory:134-162`） | 同上 | 同一概念两个主人 |
| 10 | `mmda-core-sql/.../SqlCommand.java` 整个 308 行文件被注释掉 | `mmda-core-sql/src/main/java/cloud/mmda/core/sql/SqlCommand.java` | 旧命令类残骸，读代码时勿当在用实现 |
| 11 | 测试近乎为零：mmda-core 全部测试 **527 行**；新栈只有 `SqlExpTest`/`SqlQueryTest`/`SqlCommandTest`（跑 `MockMetadataProvider`），唯一连真库的 `EntityFactoryTest` 在 mmda-base 且一半方法没有 `@Test` | `mmda-core/**/src/test/**`、`mmda-base/mmda-base-repository/src/test/java/cloud/mmda/base/data/EntityFactoryTest.java` | 重构没有回归回路 |

## 6. 重构主轴（建议顺序）

**先内核自证，再接线，最后退役旧栈。**

- **A｜内核收敛**（不动调用方）：按职责把 `EntityFactory` 拆成 `EntityRegistry`（实例级，替掉 4 个 static map）/ `EntityStore`（JdbcTemplate + CRUD）/ `EntityQuery` + `EntityCommand`（DSL 与命令构建器）/ `EntitySet`；修问题 1–7；补 core-sql + entities 的测试（含一个连真库用例）。
- **B｜接线**：让 `EntityRepository`（或新基类）内部改用 `EntityFactory`，业务侧 Repository 子类接口不变 —— 每次只改一处，426 个业务仓库类零改动。这正是 `EntityRepository.java:75` 那个字段本来要干的事。
- **C｜退役旧栈**：删 `core-data/data/sql`（SqlQuery 1350 + SqlExpression 799 + 5 个 SqlDateTime）、`DbMetadataProvider`，元数据装载归一到 `core-sql/dialects`；最后删 `Repository`/`EntityRepository`/`TenancyEntityRepository`（合计 4117 行）。

两条待拍板的横向决定：

1. `EntityFactory` 是否该留在 `entities` 模块（还是新开 `core-dao`/`core-orm`）；
2. 三级缓存的 key 现在是「实体类 + 名字」字符串，跨数据源/跨方言要不要进 key。

## 7. 构建与测试基线（2026-09-24 实测跑通）

本仓**无 VCS、`mvn` 不在 PATH**，重构前必须先把这条基线固化，否则 2178 行内核拆到一半分不清是谁弄红的。

### 7.1 怎么编译（两个坑都实测过）

必须 **JDK 21** + IDEA 2026.2.3 自带的 Maven 3.9.16。已封装成脚本：

```bash
MMDA=~/AppData/Local/hermes/bin/mmda-mvn.sh
$MMDA -C /d/2026/java/mmda-core -o -DskipTests compile    # 全量编译
$MMDA -C /d/2026/java/mmda-core test                      # 跑测试
$MMDA -v                                                  # 自检：Maven 3.9.16 + Java 21.0.2
```

脚本内部做的事（也可手抄）：

```bash
JDK="/c/Program Files/Java/jdk-21"
MH="C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.3/plugins/maven-plugin/lib/maven3"
CW=$(ls "/c/Program Files/JetBrains/IntelliJ IDEA 2026.2.3/plugins/maven-plugin/lib/maven3"/boot/plexus-classworlds-*.jar|head -1|sed 's|^/c/|C:/|')
"$JDK/bin/java" -classpath "$CW" "-Dclassworlds.conf=$MH/bin/m2.conf" "-Dmaven.home=$MH" \
  "-Dmaven.multiModuleProjectDirectory=D:/2026/java/mmda-core" \
  org.codehaus.plexus.classworlds.launcher.Launcher -o -DskipTests compile
```

两个会伪装成「仓库编译不过」的坑：

1. 直接跑 IDEA 的 `plugins/maven-plugin/lib/maven3/bin/mvn` → `ClassNotFoundException: org.codehaus.plexus.classworlds.launcher.Launcher`，因为 bash 脚本算出的 `-classpath` 是 `/c/...`，native 的 `java.exe` 解析不了。
2. 用 IDEA 的 jbr（2024.1.4 是 17、2026.2.3 是 25）→ 大面积假报「找不到符号 `getXxx()` / 未实现抽象方法 `getValue()`」，根因是 lombok 1.18.32（`mmda-core/pom.xml:79`）在这些 JDK 上注解处理不跑；换 JDK 21 即消失。

### 7.2 当前基线

**编译全绿**：`-o -DskipTests compile` → BUILD SUCCESS，14 个模块，48.5 s（上一轮成功产物停在 `2026-07-06`）。

**测试 8 跑 6 红**（`mmda-core` 全仓只有 8 个测试方法，本节是重构前的红点底账）：

| 模块 | 结果 | 红点 |
|---|---|---|
| utils | 2 跑 1 红 | `cloud.mmda.core.utils.ClassUtilTest.getGenericType:49` — `ClassCastException: TypeVariableImpl cannot be cast to Class` |
| sql | 9 跑 4 红 | `SqlCommandTest.delete:27`、`SqlCommandTest.update:45` — 断言写的是**旧 SQL 形态**（`AS t`、`UPDATE t FROM …`），新方言输出不带别名、UPDATE 是全字段；`SqlExpTest.testCreate:59` — expected `<true>` but was `<false>`；`SqlQueryTest.testCreate:28` — `IllegalArgumentException: creator relation not found inPartner` |
| entities | 2 跑 1 红 | `PartialTest.testPartial:15` — `IllegalArgumentException: name should start with 'is', 'get' or 'set'.` |
| file | 0 跑（起不来） | 测试进程直接崩：`NoClassDefFoundError … JUnit jars … conflicting versions`，属依赖版本没对齐，先修环境再看 |
| metadata / caching / data / security / messaging / services / api / reporting | 无测试 | — |

读法：sql 的两条是**断言过期**（跟着方言重构走，不是运行时真坏），`SqlExpTest`/`SqlQueryTest`/`PartialTest`/`ClassUtilTest` 四条要单独确认是「代码退化」还是「测试过期」——这正是重构 §6-A 第一步要清掉的东西。

### 7.3 仍需补的前置

- 仓库无 VCS：建议先 `git init` 打第一个基线提交（含 `target/` 之外的源码），否则后续每批重构都无法回滚。
- `mmda-core` 之外的业务模块（mes/los/fin…）走 `~/.m2/repository/cloud/mmda/*` 的制品，改 core 后需要 `install` 才能被业务侧看到；重构期间建议全程在 `mmda-core` 聚合工程内验证。
