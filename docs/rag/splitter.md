---
layout: default
title: Splitter
parent: Retrieval Augmented Generation
nav_order: 1
---

## Code Splitter

我们使用的是基于 Kotlin 语言 + Antlr 4 的 [Chapi](https://github.com/modernizing/chapi) 方案，它已经在 [ArchGuard](https://github.com/archguard/archguard)
项目中得到了验证。

## Markdown Splitter

在使用本地的 Sentence Transformers，由于受 512 的长度影响，需要控制好 token 的长度。

- 当文档越长，相似度越低。使用 markdown 时，需要关注长度带来的影响。

## Token Splitter

OpenAI Token Splitter

## Sentence Splitter


