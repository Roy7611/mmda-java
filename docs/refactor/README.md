# MMDA 服务端（D:\2026\java）重构文档

本目录是这一轮重构的唯一文档入口。所有重构相关的分析、方案、迁移记录、图表都放在这里，不再散落到模块内。

## 目录约定

```
docs/refactor/
├── README.md                 # 本文件：索引与写法约定
├── 01-现状-mmda-core.md       # 现状分析（含 file:line 证据）
├── 02-方案-*.md               # 逐个重构方案（待写）
├── 03-迁移记录-*.md           # 逐个迁移的落地记录（待写）
└── diagrams/                 # archify 产出的交互式架构图
    ├── *.architecture.json   # 图表源（typed JSON，唯一真源）
    └── *.architecture.html   # 交付产物（自包含 HTML）
```

命名：`<两位序号>-<类型>-<主题>.md`，序号只增不改。图表按 `<主题>.architecture.json|html` 命名，与引用它的文档同名。

## 写法约定

1. **结论必须带证据**：行引用一律写成仓库相对路径的 `mmda-core/.../EntityFactory.java:843`，在 VS Code 里可直接点开跳转。
2. **区分实测与推断**：改过/跑过的写「实测」，没编译没跑测试的写「未验证」。
3. **数字要能复算**：模块规模、引用点计数等，给出一条可重跑的命令。
4. **文档与代码同批**：每个重构批次落一次 `03-迁移记录-*.md`，写清「已落地 / 已否决·维持现状 / 后续 / 低优先级」四段，已否决项写清为什么不改。

## 图表流水线（archify）

图由技能 `software-development/archify` 生成（JSON 图元 → 校验 → 自包含 HTML）：

```bash
cd ~/AppData/Local/hermes/skills/software-development/archify
node bin/archify.mjs doctor                                                    # 环境自检
node bin/archify.mjs validate architecture <src.json> --quality showcase --json # 每次改动后
node bin/archify.mjs deliver  architecture <src.json> <out.html> --quality showcase --json
node bin/archify.mjs visual-check <out.html> --json                            # 真实浏览器取证
```

口径：`deliver` 必须 9/9 项检查通过、0 error 0 warning；`visual-check` 要在 1440×900 / 1600×1000 / 1920×1080 / 2048×1320 四个视口都 `ok=true`（无横向/纵向溢出）。**只改 JSON 源，不要手改 HTML**——HTML 是交付产物。

## 构建与验证（mmda-core）

本机没有 `mvn`，PATH 上的 java 是 17（工程要 21），所以封装了入口脚本（必须 JDK 21，详见 `01-现状-mmda-core.md` §7.1）：

```bash
MMDA=~/AppData/Local/hermes/bin/mmda-mvn.sh
$MMDA -C /d/2026/java/mmda-core -o -DskipTests compile   # 全量编译，14 模块
$MMDA -C /d/2026/java/mmda-core test                     # 跑测试
```

**当前基线（2026-09-24）**：编译 BUILD SUCCESS；8 个测试方法 6 红（清单与 file:line 见 `01-现状-mmda-core.md` §7.2）。每批重构后重跑这两条命令，红点只减不增。

## 文档清单

| 文档 | 内容 | 状态 |
|---|---|---|
| `01-现状-mmda-core.md` | mmda-core 现状架构、两栈并存事实、EntityFactory 问题清单、重构主轴 | 已完成 |
| `diagrams/mmda-core-current.architecture.html` | 现状架构图（可交互：主题/搜索/聚焦/导出） | 已交付（9/9 + 4 视口通过） |
| `02-方案-*.md` | 内核拆分、接线、旧栈退役的具体方案 | 待定 |
| `03-迁移记录-*.md` | 每批重构的落地记录与验证结果 | 待定 |
