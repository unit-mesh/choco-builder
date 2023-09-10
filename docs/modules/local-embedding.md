---
layout: default
title: Local Embedding
parent: Modules
nav_order: 2
---

Local Embedding 模块是一个本地矢量化模块，用于将文本句子嵌入到一个高维向量空间中，以便进行各种文本相关任务，如文本相似度计算、

## Sentence Transformers

> Sentence Transformers 是一个自然语言处理工具，用于将文本句子嵌入到一个高维向量空间中，以便进行各种文本相关任务，如文本相似度计算、
> 文本分类、聚类等。它是通过预训练的深度学习模型实现的，通常使用诸如BERT、RoBERTa、DistilBERT等预训练模型作为其基础架构。

使用示例：

```kotlin
val semantic = STSemantic.create()
val embedding = semantic.embed("what is the weather today?")
```
