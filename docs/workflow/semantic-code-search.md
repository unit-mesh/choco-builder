---
layout: default
title: Semantic Code Search
parent: Workflow
nav_order: 2
---

## 方式 1. ArchGuard CLI 上传代码

在部署完服务后，可以通过 ArchGuard CLI 直接上传代码。

下载地址：[https://github.com/archguard/archguard/releases](https://github.com/archguard/archguard/releases)

使用示例：

```
 java -jar scanner_cli-2.0.6-all.jar --language=Kotlin --output=http --server-url=https://ai.dts.plus
  --path=/Volumes/source/ai/chocolate-factory --with-function-code
```

- 注意：一定要添加 `with-function-code`，否则无法生成函数级别的语义。
- 注意：一定要添加 `with-function-code`，否则无法生成函数级别的语义。
- 注意：一定要添加 `with-function-code`，否则无法生成函数级别的语义。

部分日志参考如下：

```bash
[SCANNER] org.archguard.scanner.ctl.Runner <cli parameters>
|type: SOURCE_CODE
|systemId: 0
|serverUrl: https://ai.dts.plus
|workspace: .
|path: /Volumes/source/ai/chocolate-factory
|output: [http, json]
<customized analysers>
|analyzerSpec: []
|slotSpec: []
<additional parameters>
|language: Kotlin
|features: []
|repoId: null
|branch: master
|startedAt: 0
|since: null
|until: null
|depth: 7
|rules: []
[SCANNER] o.a.s.ctl.loader.AnalyserLoader workspace path: /Volumes/source/archguard/archguard-backend
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser install path: /Volumes/source/archguard/archguard-backend/dependencies/analysers
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser: kotlin - [2.0.6] is installed
```


