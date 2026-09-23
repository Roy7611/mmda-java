#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 mmda-core 源码生成「模块架构图」（PlantUML）。

真源：mmda-core/*/pom.xml（模块依赖）+ src/main/java（类与关系实测）。
产物：本目录 *.png + wsd/*.wsd。可反复重跑；重构后重跑即刷新，不手改产物。

用法：python gen-diagrams.py [--no-render]
"""
import os, re, sys, glob, json, collections, subprocess

HERE = os.path.dirname(os.path.abspath(__file__))
PROJ = os.path.abspath(os.path.join(HERE, "..", "..", ".."))
CORE = os.path.join(PROJ, "mmda-core")
WSD = os.path.join(HERE, "wsd")
CHARSET = 'skinparam defaultFontName "Microsoft YaHei"\nskinparam defaultFontSize 12\nskinparam shadowing false\nset namespaceSeparator none\n'

TYPE_RE = re.compile(r"^\s*(?:public\s+|final\s+|abstract\s+|sealed\s+|non-sealed\s+|static\s+)*(class|interface|enum|record|@interface)\s+(\w+)", re.M)
EXT_RE = re.compile(r"\bextends\s+([\w.]+)")
IMP_RE = re.compile(r"\bimplements\s+([\w.,\s<>]+?)\s*\{")
IMPORT_RE = re.compile(r"^\s*import\s+(static\s+)?([\w.]+)\s*;", re.M)


def scan():
    mods = {}
    for m in sorted(os.listdir(CORE)):
        d = os.path.join(CORE, m)
        if not (os.path.isdir(d) and m.startswith("mmda-core-")): continue
        pom = open(os.path.join(d, "pom.xml"), encoding="utf-8", errors="replace").read() if os.path.exists(os.path.join(d, "pom.xml")) else ""
        comp, jar = [], []
        for dep in re.findall(r"<dependency>(.*?)</dependency>", pom, re.S):
            a = re.search(r"<artifactId>([^<]+)</artifactId>", dep); g = re.search(r"<groupId>([^<]+)</groupId>", dep)
            if not a or a.group(1) == m or (g and g.group(1) != "cloud.mmda"): continue
            (jar if "<scope>system</scope>" in dep else comp).append(a.group(1))
        classes = {}
        src = os.path.join(d, "src", "main", "java")
        for dp, dn, fn in os.walk(src):
            for f in fn:
                if not f.endswith(".java"): continue
                p = os.path.join(dp, f)
                t = open(p, encoding="utf-8", errors="replace").read()
                pm = re.search(r"^package\s+([\w.]+);", t, re.M)
                tm = TYPE_RE.search(t)
                if not tm or tm.group(2) != f[:-5]: continue
                imm = IMP_RE.search(t)
                classes[f"{(pm.group(1) if pm else '')}.{tm.group(2)}"] = {
                    "pkg": pm.group(1) if pm else "", "short": tm.group(2), "kind": tm.group(1),
                    "abstract": bool(re.search(r"^\s*(?:public\s+)?abstract\s+class", t, re.M)) or tm.group(1) in ("interface", "@interface"),
                    "lines": t.count("\n") + 1,
                    "extends": [x.split("<")[0].split(".")[-1] for x in EXT_RE.findall(t)[:2]],
                    "implements": [] if not imm else [x.strip().split("<")[0].split(".")[-1] for x in imm.group(1).split(",") if x.strip()],
                    "imports": [i[1] for i in IMPORT_RE.findall(t)],
                    "text": t, "path": os.path.relpath(p, PROJ).replace("\\", "/"),
                }
        mods[m] = {"comp": sorted(set(comp)), "jar": sorted(set(jar)), "classes": classes,
                   "test": sum(1 for dp, dn, fn in os.walk(os.path.join(d, "src", "test")) for f in fn if f.endswith(".java"))}
    return mods


def relations(c):
    cls = c["classes"]
    byshort = collections.defaultdict(list)
    for fq, x in cls.items(): byshort[x["short"]].append(fq)
    inherit, refs = [], collections.Counter()
    for fq, x in cls.items():
        clean = "\n".join(l for l in x["text"].splitlines() if not l.strip().startswith(("import ", "package ")))
        clean = re.sub(r"\b(class|interface|enum|record)\s+" + re.escape(x["short"]) + r"\b", r"\1 _SELF_", clean)
        for t in x["extends"] + x["implements"]:
            for dst in byshort.get(t, ()):
                if dst != fq: inherit.append((fq, dst, "extends" if t in x["extends"] else "impl"))
        for imp in x["imports"]:
            if imp in cls and imp != fq: refs[(fq, imp)] += 1
        for short, lst in byshort.items():   # 同包引用不写 import，必须按短名匹配
            if short == x["short"]: continue
            if re.search(r"\b" + re.escape(short) + r"\b", clean):
                for dst in lst:
                    if dst != fq: refs[(fq, dst)] += 1
    ind = collections.Counter()
    for a, b, k in inherit: ind[b] += 2
    for (a, b), w in refs.items(): ind[b] += min(w, 3)
    return inherit, refs, ind


def w(name, body): open(os.path.join(WSD, name + ".wsd"), "w", encoding="utf-8").write(body)


def gen_module(m, c):
    cls = c["classes"]
    if not cls: return None
    inherit, refs, ind = relations(c)
    nmax = 26 if len(cls) > 60 else 34
    keep = [f for f in sorted(cls, key=lambda f: (-ind[f], f)) if ind[f] > 0][:nmax]
    ks = set(keep)
    inherit_k = sorted(e for e in inherit if e[0] in ks and e[1] in ks)
    minw = 2 if len(keep) > 20 else 1
    refs_k = sorted(((a, b), v) for (a, b), v in refs.items() if a in ks and b in ks and v >= minw)[:26]
    lines = sum(x["lines"] for x in cls.values())
    t = ("@startuml " + m + "\n" + CHARSET + "skinparam nodesep 12\nskinparam ranksep 26\n"
         "skinparam packageStyle rectangle\nhide circle\nhide empty members\n"
         f"title {m}\u3000类关系图（{len(cls)} 个类型 / {lines:,} 行）\n"
         "legend right\n"
         f"  图中节点 {len(keep)} / {len(cls)} 个：按模块内被引用度排序取前 {nmax}\n"
         "  实线空心箭头=extends；虚线空心箭头=implements\n"
         "  <u>虚线开放箭头=类型引用</u>（数字=引用它的文件数，\u22652 才画）\n"
         "  真源：src/main/java 源码实测（import + 同包名引用 + extends/implements）\nendlegend\n")
    alias = {fq: "T%d" % i for i, fq in enumerate(keep)}
    groups = collections.defaultdict(list)
    for fq in keep: groups[cls[fq]["pkg"]].append(fq)
    for pkg in sorted(groups):
        label = pkg.split(".", 3)[-1] if pkg.startswith("cloud.mmda.core") else pkg
        t += 'package "%s" {\n' % label
        for fq in sorted(groups[pkg], key=lambda x: x.split(".")[-1]):
            x = cls[fq]
            st = "interface" if x["kind"] in ("interface", "@interface") else ("enum" if x["kind"] == "enum" else ("abstract class" if x["abstract"] else "class"))
            t += f'  {st} "{x["short"]}" as {alias[fq]}\n'
        t += "}\n"
    for a, b, k in inherit_k:
        t += f'{alias[b]} <|{"--" if k == "extends" else ".."} {alias[a]}\n'
    for (a, b), v in sorted(refs_k, key=lambda x: x[0]):
        t += f"{alias[a]} ..> {alias[b]} : {v}\n"
    t += ("footer\n  <b>模块外依赖（真源 pom.xml）</b>\u3000源码模块：" + (" · ".join(c["comp"]) or "—") +
          "\u3000|\u3000预编译 jar（scope=system）：" + (" · ".join(c["jar"]) or "—") + "\nendfooter\n@enduml\n")
    w(m.replace("mmda-core-", "core_"), t)
    return dict(classes=len(cls), lines=lines, shown=len(keep), inh=len(inherit_k), ref=len(refs_k), test=c["test"])


def gen_overview(mods):
    def size(m):
        c = mods[m]["classes"]
        return f'{len(c)} 类型 / {sum(x["lines"] for x in c.values()):,} 行' if c else "无源码 · 空壳"
    frames = [("基础层", ["mmda-core-utils"]),
              ("元数据 / 实体层", ["mmda-core-metadata", "mmda-core-entities"]),
              ("SQL 内核（新栈）", ["mmda-core-sql"]),
              ("数据访问层（旧栈主干 + 缓存）", ["mmda-core-data", "mmda-core-caching"]),
              ("服务与接口层", ["mmda-core-services", "mmda-core-api"]),
              ("横切能力", ["mmda-core-file", "mmda-core-security", "mmda-core-messaging"]),
              ("空壳 / 聚合", ["mmda-core-reporting"])]
    t = ("@startuml mmda_core_modules\n" + CHARSET +
         "skinparam defaultFontSize 13\nskinparam componentStyle rectangle\nskinparam nodesep 18\nskinparam ranksep 55\n"
         "skinparam linetype ortho\nskinparam ArrowColor #6a6a6a\n"
         "title mmda-core 5.0.0\u3000模块架构与依赖（%d 个 module）\n" % len(mods))
    for name, ms in frames:
        t += 'package "%s" {\n' % name
        for m in ms:
            if m not in mods: continue
            t += f'  component "{m}\\n{size(m)}" as M_{m.replace("mmda-core-", "")}\n'
        t += "}\n"
    t += 'component "mmda-core-dependencies\\n只导出 BOM" as M_dep\nM_dep .[hidden].> M_dep\n'
    n, jars = 0, []
    for m, c in mods.items():
        if m == "mmda-core-dependencies": continue
        a = "M_" + m.replace("mmda-core-", "")
        for d in c["comp"]:
            if d.startswith("mmda-core-") and d != "mmda-core-dependencies":
                t += f"{a} --> M_{d.replace('mmda-core-', '')}\n"; n += 1
        for d in c["jar"]:
            if d.startswith("mmda-core-"): jars.append((a[2:], d.replace("mmda-core-", "")))
    t += ("legend right\n  <b>依赖口径（真源 mmda-core/*/pom.xml）</b>\n"
          f"  实线 = 源码模块依赖（scope=compile），{n} 条\n"
          f"  另有 {len(jars)} 条依赖走 scope=system 的预编译 jar（未画线）：\n  " +
          " · ".join(f"{a}\u2192{b}" for a, b in jars) +
          "\n  该 jar 在 ${mmda.local.repository}（mmda-core/pom.xml:47）下，"
          "改上游源码后必须重新 install 才生效\n"
          "  mmda-core-reporting：目录 + pom 在、无源码\uff1bmmda-core-dependencies：只导出 BOM\nendlegend\n@enduml\n")
    w("core_modules", t)


def gen_callchain(mods):
    """运行链路图：类名与行数从源码取，缺类则标注「未找到」。"""
    idx = {x["short"]: x for m in mods for x in mods[m]["classes"].values()}
    def ln(*names): return " / ".join(f'{n} ({idx[n]["lines"]})' if n in idx else f"{n} (?)" for n in names)
    t = ("@startuml mmda_core_callchain\n" + CHARSET +
         "skinparam defaultFontSize 12\nskinparam componentStyle rectangle\nskinparam nodesep 14\nskinparam ranksep 30\n"
         "skinparam ArrowColor #4a4a4a\n"
         "title mmda-core 运行链路：一次「元数据驱动」的实体请求\n"
         "legend\n  括号内为源码实测行数（src/main/java）。\n"
         "  两套数据访问栈并存：旧栈与 / 新栈的 import 引用点计数见 README.md。\nendlegend\n"
         'database "metadata 库表\\nMetaCol / MetaUiField / MetaUiFieldI18nt / module" as DB\n'
         'package "元数据装载（-data + -metadata）" {\n'
         f'  component "SqlMetadataProvider\\n({idx.get("SqlMetadataProvider",{}).get("lines","?")} 行)\\n'
         f'implements MetadataProvider ({idx.get("MetadataProvider",{}).get("lines","?")} 行)" as MP\n'
         f'  component "MetaObject ({idx.get("MetaObject",{}).get("lines","?")})\\n{ln("MetaCol", "MetaContext")}" as MO\n}}\n'
         "DB --> MP : \"SELECT 装载\"\nMP --> MO\n"
         'package "SQL 构建：旧栈（-data/data/sql）" {\n'
         f'  component "{ln("SqlQuery", "SqlExpression", "SqlBuilder")}" as OQ\n}}\n'
         'package "SQL 内核：新栈（-sql / -entities）" {\n'
         f'  component "SqlDialect ({idx.get("SqlDialect",{}).get("lines","?")}) → EntityFactory ({idx.get("EntityFactory",{}).get("lines","?")})\\n'
         f'→ EntityClassAccess ({idx.get("EntityClassAccess",{}).get("lines","?")})" as NF\n'
         f'  component "{ln("SqlQueryable", "SqlCmd", "SqlExecutable")}" as NC\n}}\n'
         "MO --> OQ : \"cols/relations 拼 SELECT·JOIN\"\nMO ..> NF : \"新栈经方言取 MetadataProvider\"\nNF --> NC\n"
         'package "数据访问（-data/jdbc/repository）" {\n'
         f'  component "{ln("Repository", "TenancyRepository", "EntityRepository", "TenancyEntityRepository")}" as REPO\n}}\n'
         "OQ --> REPO : \"SQL 文本\"\nNC ..> REPO : \"JdbcTemplate\"\n"
         'package "服务（-services + -caching）" {\n'
         f'  component "{ln("DomainService", "EntityService", "TenancyEntityService", "FlowableEntityService")}" as SVC\n'
         f'  component "{ln("EntityCacheProvider", "CacheProvider", "CachePolicy")}" as CACHE\n}}\n'
         "REPO --> SVC : \"行 → 实体\"\nSVC <--> CACHE : \"Redis 读穿/写穿\"\n"
         'package "接口（-api）" {\n'
         f'  component "{ln("ReactiveApiController", "ReactiveEntityController")}\\n≈50 个通用端点" as API\n}}\n'
         "SVC --> API\nAPI --> [前端 TS：getRows / filterRows]\n@enduml\n")
    w("core_callchain", t)


def find_jar():
    cands = [os.path.join(os.environ.get("LOCALAPPDATA", ""), "plantuml", "plantuml.jar")]
    cands += glob.glob(os.path.expanduser("~/.vscode/extensions/jebbs.plantuml-*/plantuml.jar"))
    return next((c for c in cands if os.path.isfile(c)), None)


def main():
    os.makedirs(WSD, exist_ok=True)
    mods = scan()
    stat = {m: gen_module(m, c) for m, c in mods.items()}
    gen_overview(mods); gen_callchain(mods)
    print(json.dumps({k: v for k, v in stat.items() if v}, ensure_ascii=False, indent=1))
    if "--no-render" in sys.argv: return
    jar = find_jar()
    if not jar: print("未找到 plantuml.jar，只生成了 .wsd"); return
    files = sorted(glob.glob(os.path.join(WSD, "*.wsd")))
    r = subprocess.run(["java", "-jar", jar, "-charset", "UTF-8", "-tpng", "-failfast2",
                        "-Playout=smetana", "-o", HERE] + files, capture_output=True, text=True)
    print("render rc =", r.returncode, r.stdout.strip()[-300:], r.stderr.strip()[-300:])


if __name__ == "__main__":
    main()
