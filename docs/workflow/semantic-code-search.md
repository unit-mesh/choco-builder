---
layout: default
title: Semantic Code Search 示例
parent: Workflow
nav_order: 2
---

Semantic Code Search 是一类代码库 AI 助手。

> 代码库 AI 助手是一种智能工具，通常使用自然语言处理和机器学习技术，旨在帮助开发者更有效地管理和理解大型代码库。
> 它能够回答关于代码库的问题、提供文档、搜索代码、识别错误源头、减少代码重复等，从而提高开发效率、降低错误率，并减轻开发者的工作负担。

我们依旧采用的是 DDD 思想所构建的。

第一部分，围绕于问题空间的求解，对用户问题进行分析与转换，以获得潜在的解决方案。

第二部分，围绕于解决空间的实现，即通过检索增强（RAG，Retrieval Augmented Generation），来获得对应问题的答案。

为此，在第一部分，我们将会分析问答的问题，以构建出一个针对于解决方案的 DSL。然后，围绕于 DSL 来进行检索，获得相应的答案，最后交由
LLM 来进行总结。

## Prompt 构建策略阶段 1：问题求解

在设计上，为了更好的进进行检索，在设计 CoUnit 时，我们拆分成三种检查条件：

- `englishQuery`，将中文翻译成英文，再结合英文进行搜索。
- `originLanguageQuery`，如我们在使用中文里，翻译成英语可能不标准，但是注释中则可能是使用中文存在，所以相似式也会很靠谱。
- `hypotheticalDocument`，假设性文档，即根据用户的请求生成代码，再结合生成的代码进行相似式搜索。

所以，在阶段一就需要由 LLM 来分析用户的问题，并给出如上的三个检查条件。`hypotheticalDocument` 参考的是 Bloop 的设计：

- `hypotheticalDocument` is a code snippet that could hypothetically be returned by a code search engine as the answer.
- `hypotheticalDocument` code snippet should be between 5 and 10 lines long

不过，由于一次给了三个条件，偶尔还是存在概率性的假设性文档出错的问题。

## Prompt 构建策略阶段 2：检索增强

在现有的设计里，一个代码库 AI 助手本质也是 RAG（检索增强，Retrieval Augmented Generation），因此可以分为 indexing 阶段和
querying 阶段。

### 代码库 AI 助手：indexing 阶段

在 indexing 阶段，基本上就是：

- 文本分割（TextSplitter）。负责将源数据分割成较小单元（Chunks）的工具或组件。
- 文本向量化（Vectoring）。负责将拆分好的 Chunk 转变化向量化数组。
- 数据库（Vector Database）负责通过高效的向量检索技术来实现文档片段的快速检索。

在文本向量化上，我们使用的是 `SentenceTransformer` 的本地化极小 NLP 模型（22M 左右）。对于代码来说，它是结构化的形式，并且也经过了
GitHub Copilot、Bloop 的充分验证，所以准确度并不差。

由于使用的是本地化模型，通过 CPU 就可以快速计算完成，所以更新策略上可以和 CI、CD 集成。一旦有代码更新时，就可以 indexing。

### 代码库 AI 助手：querying 阶段

在 querying 阶段，我们会围绕阶段 1 的 DSL，先转换 DSL 的文本成对应的向量化形式。

再对其进行对应的内容检索：

```kotlin
// 基于英语的相关代码列表
val list = store.findRelevant(query, 15, 0.6)
// 基于中文的相关代码列表
val originLangList = store.findRelevant(originQuery, 15, 0.6)
// 相关的假设性代码列表
val hydeDocs = store.findRelevant(hypotheticalDocument, 15, 0.6)        
```

随后，再对结果进行排序。考虑到诸如 《[Lost in the Middle: How Language Models Use Long Contexts](https://arxiv.org/abs/2307.03172)
》对于长文本的影响，我们在 CF 中也引入了对应的方式，因此一个排序后的代码结果如下所示：

```kotlin
0.7847863 // canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflowTest
0.76635444 // canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticDecl
0.74648994 // canonicalName: cc.unitmesh.cf.core.flow.ProblemAnalyzer
0.7410852 // canonicalName: cc.unitmesh.cf.domains.spec.SpecDomainDecl
0.72767156 // canonicalName: cc.unitmesh.cf.core.flow.DomainDeclaration
0.73245597 // canonicalName: cc.unitmesh.cf.core.flow.model.WorkflowResult
0.7434818 // canonicalName: cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow.execute
0.757218 // canonicalName: cc.unitmesh.cf.core.flow.Workflow
0.7722022 // canonicalName: cc.unitmesh.cf.domains.semantic.flow.SemanticProblemAnalyzer
0.807935 // canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow.execute
```

前面的数字便是相关性的 score，后面的 canonicalName 则是相关代码的信息，可能是类、类方法、方法等。

## Prompt 策略 3：代码拆分策略

在代码的分割上，不同的框架有不同的策略，LangChain 是基于关键字的方式，LlamaIndex 是基于 TreeSitter 方式，Bloop 则是基于
TreeSitter 的 S-表达式创建。

我们则是采用 ArchGuard Scanner 的标准化代码结构方式，除了可以直接利用 ArchGuard 项目的现有的 CLI
基础设施，在形式上与结果上也更加准确。诸如于可以针对于 Interface、abstract class 等进行优化 Chunk 的拆分规则，并构建出
canonicalName 的方式，来方便索引和定位。

### 代码拆分规则

我们参考的折分规则是：https://docs.sweep.dev/blogs/chunking-2m-files 。即：

1. 代码的平均 Token 到字符比例约为1:5（300 个 Token），而嵌入模型的 Token 上限为 512 个。
2. 1500 个字符大约对应于 40 行，大致相当于一个小到中等大小的函数或类。
3. 挑战在于尽可能接近 1500 个字符，同时确保分块在语义上相似且相关上下文连接在一起。

对应的模型如下：

```kotlin
class CodeSplitter(
    private val comment: String = "//",
    private val chunkLines: Int = 40,
    private val maxChars: Int = 1500,
    // TODO: for unsupported languages, we can use the following heuristic to split the code
    val chunkLinesOverlap: Int = 15,
)
```

当然了，这里的 chunkLinesOverlap 是还没有实现的。

### 代码上传方式. ArchGuard CLI 上传代码

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
