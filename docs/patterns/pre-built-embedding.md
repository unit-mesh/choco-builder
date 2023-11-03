---
layout: default
title: 预构建 Embedding Indexes
parent: Patterns
nav_order: 99
---

预构建 Embedding Indexes 是一种向量化索引数据，即将系统的一些内置的知识向量化，并可以以文件的形式进行导出和下载，以便于用户在本地进行使用。

示例：JetBrains AI Assistants 中的预定义 Action 等构建为 Embedding Indexes，减少了用户的本地工作量。

示例文件：[semantic-text-search-0.0.3.jar](https://packages.jetbrains.team/maven/p/ml-search-everywhere/local-models/org/jetbrains/intellij/searcheverywhereMl/semantics/semantic-text-search/0.0.3/semantic-text-search-0.0.3.jar)

```bash
semantic-text-search-0.0.3
├── META-INF
│   └── MANIFEST.MF
├── actions
│   ├── embeddings.bin  # 预构建的 Embedding Indexes
│   └── ids.json        # 预构建的 Embedding Indexes 的 ID
└── models
    ├── dan-bert-tiny
    │   ├── encoder
    │   │   └── tokenizer_config.json
    │   └── optimized
    │       └── dan_optimized_fp16.onnx
    └── encoder
        └── bert-base-uncased.txt
```
