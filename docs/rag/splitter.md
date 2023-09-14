---
layout: default
title: Splitter
parent: Retrieval Augmented Generation
nav_order: 1
---

## Code Splitter

我们使用的是基于 Kotlin 语言 + Antlr 4 的 [Chapi](https://github.com/modernizing/chapi)
方案，它已经在 [ArchGuard](https://github.com/archguard/archguard)
项目中得到了验证。

## Markdown Splitter

在使用本地的 Sentence Transformers，由于受输出是 384 的长度影响，需要控制好 token 的长度。因此，需要对文档进行**二次分割**。

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
