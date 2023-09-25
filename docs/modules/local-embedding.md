---
layout: default
title: Local Embedding
parent: Modules
nav_order: 2
permalink: /docs/local-embedding
---

Local Embedding 模块是一个本地矢量化模块，用于将文本句子嵌入到一个高维向量空间中，以便进行各种文本相关任务，如文本相似度计算等。
Local Embedding
主要依赖于 ONNX 来进行本地推理，诸如于 SentenceTransformers 等模型。

> Onnx 是一个跨平台机器学习推理加速器。通常用于在客户端、服务端引入小模型推理，诸如于引入 SentenceTransformers 在本地进行相似式搜索。
> 从实现上，ONNX 使用的是 C++ 实现的，所以其它语言下使用的也是 FFI 的形式。

## Sentence Transformers

> Sentence Transformers 是一个自然语言处理工具，用于将文本句子嵌入到一个高维向量空间中，以便进行各种文本相关任务，如文本相似度计算、
> 文本分类、聚类等。它是通过预训练的深度学习模型实现的，通常使用诸如BERT、RoBERTa、DistilBERT等预训练模型作为其基础架构。

SentenceTransformers 在体积上只有 22M，因此被 Bloop、GitHub Copilot 作为本地向量化模型，也因此是 Chocolate Factory
的默认的本地矢量化模块。

### Embedding 示例

```kotlin
val semantic = STSemantic.create()
val embedding = semantic.embed("what is the weather today?")
```

### Decode 和 Encode 示例

```kotlin
val semantic = STSemantic.create()
val embedding = semantic.getTokenizer().encode("blog")

embedding.ids shouldBe listOf(101L, 9927L, 102L)

embedding.attentionMask shouldBe listOf(1L, 1L, 1L)

val text = semantic.getTokenizer().decode(embedding.ids)
text shouldBe "[CLS] blog [SEP]"
```
