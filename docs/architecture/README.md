# MMDA 架构图（Java 仓 D:\2026\java）

本目录的图**由源码生成，不是手绘**：真源 = 各模块 `pom.xml`（模块依赖）+ `src/main/java`（类型与 `extends`/`implements`/引用关系实测），
经 PlantUML 渲成 PNG。目录中的 `.wsd` 是图的可读真源，PNG 是产物 —— 两者都由 `mmda-core/gen-diagrams.py` 重跑生成，不手改。

> 关于 IDEA：IDEA 的 MCP 接口（57 个工具）只提供读写文件、符号检索、构建、调试、数据库能力，
> **不暴露 UML / 模块依赖图的导出**（Diagrams → Show Diagram 属 IDE UI action）。
> 因此这里改用 IDEA 出图的同一份数据源：模块依赖取 `pom.xml` / `.idea/modules.xml`，类关系取源码中的 import 与继承。

## 重新生成

```bash
cd D:/2026/java/docs/architecture/mmda-core
python gen-diagrams.py            # 重扫源码 + 重渲全部 PNG
python gen-diagrams.py --no-render  # 只重写 wsd/*.wsd
```

重构 core、改了模块 pom 或类关系后重跑即可刷新，无需手工编辑图源。
渲染用 `%LOCALAPPDATA%\plantuml\plantuml.jar`（找不到则只产出 `.wsd`，可用 IDEA/VSCode 的 PlantUML 插件打开 `wsd/` 预览）。

## 图清单

### 总览

| 图 | 内容 |
|---|---|
| [mmda-core 模块架构](mmda-core/mmda_core_modules.png) | 13 个 module 的分层与依赖；实线=源码模块依赖，虚线口径与 16 条 `scope=system` 预编译 jar 依赖见图内 legend |
| [mmda-core 运行链路](mmda-core/mmda_core_callchain.png) | 一次「元数据驱动」请求的调用链，以及新旧两套数据访问栈的分叉点（括号内为源码实测行数） |

真源：[mmda-core/wsd/core_modules.wsd](mmda-core/wsd/core_modules.wsd)、[mmda-core/wsd/core_callchain.wsd](mmda-core/wsd/core_callchain.wsd)

### 逐模块类关系图

| 模块 | 类型/行 | 测试文件 | 源码模块依赖 (compile) | 预编译 jar (scope=system) |
|---|---|---|---|---|
| [mmda-core-api](mmda-core/mmda-core-api.png) | 14 / 3,372 | 0 | mmda-core-caching · mmda-core-data · mmda-core-entities · mmda-core-metadata · mmda-core-security · mmda-core-services | mmda-core-utils |
| [mmda-core-caching](mmda-core/mmda-core-caching.png) | 10 / 1,765 | 0 | — | mmda-core-entities · mmda-core-metadata · mmda-core-utils |
| [mmda-core-data](mmda-core/mmda-core-data.png) | 68 / 12,083 | 0 | — | mmda-core-entities · mmda-core-metadata · mmda-core-utils |
| mmda-core-dependencies（无类关系图） | 无源码 | 0 | mmda-core-api · mmda-core-caching · mmda-core-data · mmda-core-entities · mmda-core-messaging · mmda-core-metadata · mmda-core-security · mmda-core-services · mmda-core-utils | — |
| [mmda-core-entities](mmda-core/mmda-core-entities.png) | 52 / 6,071 | 1 | mmda-core-metadata · mmda-core-sql · mmda-core-utils | — |
| [mmda-core-file](mmda-core/mmda-core-file.png) | 50 / 9,632 | 1 | — | mmda-core-data · mmda-core-entities · mmda-core-metadata |
| [mmda-core-messaging](mmda-core/mmda-core-messaging.png) | 36 / 2,160 | 0 | mmda-core-utils | — |
| [mmda-core-metadata](mmda-core/mmda-core-metadata.png) | 105 / 11,581 | 0 | mmda-core-utils | — |
| mmda-core-reporting（无类关系图） | 无源码 | 0 | — | — |
| [mmda-core-security](mmda-core/mmda-core-security.png) | 35 / 2,795 | 0 | mmda-core-entities · mmda-core-metadata · mmda-core-utils | — |
| [mmda-core-services](mmda-core/mmda-core-services.png) | 32 / 6,409 | 0 | mmda-core-file · mmda-core-messaging | mmda-core-caching · mmda-core-data · mmda-core-entities · mmda-core-metadata · mmda-core-security · mmda-core-utils |
| [mmda-core-sql](mmda-core/mmda-core-sql.png) | 56 / 65,502 | 7 | mmda-core-metadata · mmda-core-utils | — |
| [mmda-core-utils](mmda-core/mmda-core-utils.png) | 25 / 4,152 | 2 | — | — |

`mmda-core-reporting`（目录 + pom 在、无源码）与 `mmda-core-dependencies`（只导出 BOM）无类关系图。

## 实测数据（2026-09-24）

| 事实 | 数据 | 口径 |
|---|---|---|
| 旧数据栈引用点 | 350 个文件 import `cloud.mmda.core.data.sql.*` | 全工程，排除 `target/` |
| 新数据栈引用点 | 25 个文件 import `cloud.mmda.core.sql.*`（core 内 21 / base 内 4） | 同上 |
| `mmda-core-sql` 规模 | 65,502 行，其中 `antlr4` 包 59,032 行（90%）为生成代码 | `src/main/java` |
| core 测试文件 | 11 个（`-sql` 7 / `-utils` 2 / `-entities` 1 / `-file` 1） | `src/test/java` |
| 走预编译 jar 的模块依赖 | 16 条 | `<scope>system</scope>` + `<systemPath>${mmda.local.repository}/…` |

**`${mmda.local.repository}` 的取值**：`C:/Users/${user.name}/.m2/repository/cloud/mmda`（定义在 `mmda-core/pom.xml:47`）。
`-api → -utils`、`-services → {utils,entities,metadata,caching,data,security}` 等多条依赖并不走源码模块，
而是走该目录下的 `mmda-core-*-5.0.0.jar`。**结论：改了上游模块源码后若不 `install`，下游编译拿到的仍是旧 jar。**

## 口径与局限

- 类关系图只画**模块内**关系；跨模块依赖写在图底部 footer（源码模块 / 预编译 jar 分列）。
- 节点按「模块内被引用度」截断：>60 个类型的模块取前 26 个，其余取前 34 个，图内 legend 注明实际数量 —— 低连接度的工具类不会出现在图上。
- 引用边只在 `模块内引用它的文件数 ≥ 2`（节点多于 20 个时）才画，避免 100+ 类的模块糊成一团。
- 图反映的是**当前工作区源码**（`src/main/java`），不含 `target/` 下的历史产物，不代表当前可编译。
