---
layout: default
title: Splitter
parent: Retrieval Augmented Generation
nav_order: 2
---

分块 Chunking (参考来源：[主流应用RAG的介绍](https://www.luxiangdong.com/2023/09/25/ragone/)
是将文档切割成多个块，以便于进行向量化。通常来说，会用如下的几种方式：

- 固定大小的分块方式：一般是 256/384/512 个 tokens，取决于 embedding
模型的情况。但是这种方式的弊端是会损失很多语义，比如“我们今天晚上应该去吃个大餐庆祝一下”，很有可能就会被分在两个 chunk 里面——“我们今天晚上应该”、“去吃个大餐庆祝一下”。这样对于检索是非常不友好的，解决方法是增加冗余量，比如 512 tokens 的，实际保存 480 tokens，一头一尾去保存相邻的 chunk 头尾的 tokens 内容；
- 基于意图的分块方式：
    - 句分割：最简单的是通过句号和换行来做切分。当然也有通过专业的意图包来切分的，常用的意图包有基于 NLP 的 NLTK 和 spaCy；
    - 递归分割：通过分治的方法，用递归切分到最小单元的一种方式；
    - 特殊分割：还有很多不常见的，用于特殊场景，这里就不提了。
- 影响分块策略的因素：
    - 取决于索引类型，包括文本类型和长度，文章和微博推文的分块方式就会很不同；
    - 取决于模型类型：不同的 LLM 结果也是不同的，如 ChatGLM、ChatGPT 和 Claude.ai 等的 tokens 限制长度不一样，会影响分块的尺寸；
    - 取决于问答的文本的长度和复杂度：最好问答的文本长度和分块的尺寸差不多，这样会对检索效率更友好；
    - 应用类型：根据不同的 RAG 类型（如检索、问答和摘要等），都会对分块策略有不同的影响。

在 Chocolate Factory 中，我们会结合代码的特点，使用不同的分块策略，再结合固定大小的分块方式，来进行分块。

## Code Splitter

我们使用的是基于 Kotlin 语言 + Antlr 4 的 [Chapi](https://github.com/modernizing/chapi)
方案，它已经在 [ArchGuard](https://github.com/archguard/archguard)
项目中得到了验证。

1. 下载 ArchGuard Scanner
   CLI，地址：[https://github.com/archguard/archguard/releases](https://github.com/archguard/archguard/releases)
2. 上传代码，用于 indexing，并存储到 ElasticSearch ：

```
java -jar scanner_cli-2.0.5-all.jar --language=Kotlin --output=http --server-url=http://localhost:18080 --path=/Volumes/source/ai/chocolate-factory
```

### 示例

诸如

- Interface 在长度少于 384 token 的情况下，会变为一个单独的 chunk。
- Class 在长度少于 384 token 的情况下，会变为一个单独的 chunk。
- Class 在长度大于 384 token 的情况下，会变为多个基于 Function 的 chunk。
- Function 在长度少于 384 token 的情况下，会变为一个单独的 chunk。
- Function 在长度大于 384 token 的情况下，会被切割为一个长度为 384 的 chunk。

## Markdown Splitter

在使用本地的 embedding 模型（all-MiniLM-L6-v2），由于受输出是 384 的长度影响，需要控制好 token 的长度。
因此，需要对文档进行**二次分割**。

示例：

```kotlin
// 按 H1, H2 切割
val headersToSplitOn: List<Pair<String, String>> = listOf(
    Pair("#", "H1"),
    Pair("##", "H2"),
)

val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
    .splitText(text)

// 按 384 长度切割
val documentList = documents.map {
    val header = "${it.metadata["H1"]} > ${it.metadata["H2"]}"
    val withHeader = it.copy(text = "$header ${it.text}")
    TokenTextSplitter(chunkSize = 384).apply(listOf(withHeader)).first()
}
```

因此，一个长度为 512 的文档，可能会被切割成 2 个文档，第一个的文本长度为 384，第二个的文本长度为 128。

注意：

- 当文档越长，相似度越低。使用 markdown 时，需要关注长度带来的影响。

## Token Splitter

默认使用的是：OpenAI 的 TikToken
