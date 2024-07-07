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

## Local Embedding

> Sentence Transformers 是一个自然语言处理工具，用于将文本句子嵌入到一个高维向量空间中，以便进行各种文本相关任务，如文本相似度计算、
> 文本分类、聚类等。它是通过预训练的深度学习模型实现的，通常使用诸如BERT、RoBERTa、DistilBERT等预训练模型作为其基础架构。

在这里，我们使用的 SentenceTransformers 模型是：[sentence-transformers/all-MiniLM-L6-v2](https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2)， 
在体积上只有 22M，因此被 Bloop、GitHub Copilot 作为本地向量化模型，也因此是 ChocoBuilder 的默认的本地矢量化模块。

- all-MiniLM-L6-v2 支持转为 384 维稠密向量空间（dimensional dense vector space），即 384 

可以使用 [optimum](https://github.com/huggingface/optimum) 优化模型，将模型转换为 ONNX 格式，以便于在本地进行推理。

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

## 其它 Sentence Transformers 模型

### M3E  （中文支持好）

M3E 是 Moka Massive Mixed Embedding 的缩写

- Moka，此模型由 MokaAI 训练，开源和评测，训练脚本使用 [uniem](https://github.com/wangyuxinwhy/uniem/blob/main/scripts/train_m3e.py) ，评测 BenchMark 使用 [MTEB-zh](https://github.com/wangyuxinwhy/uniem/tree/main/mteb-zh)
- Massive，此模型通过**千万级** (2200w+) 的中文句对数据集进行训练
- Mixed，此模型支持中英双语的同质文本相似度计算，异质文本检索等功能，未来还会支持代码检索
- Embedding，此模型是文本嵌入模型，可以将自然语言转换成稠密的向量

[M3E](https://huggingface.co/moka-ai/m3e-base) 分为 small 和 base

M3E 对应的 Tips

- 使用场景主要是中文，少量英文的情况，建议使用 m3e 系列的模型
- 多语言使用场景，并且不介意数据隐私的话，我建议使用 text-embedding-ada-002
- 代码检索场景，推荐使用 text-embedding-ada-002
- 文本检索场景，请使用具备文本检索能力的模型，只在 S2S 上训练的文本嵌入模型，没有办法完成文本检索任务

## Intellij

### SearchEverywhere—SemanticSearch

估计可能是这个版本：[https://huggingface.co/prajjwal1/bert-tiny](https://huggingface.co/prajjwal1/bert-tiny)

```json
{
  "clean_up_tokenization_spaces": true,
  "cls_token": "[CLS]",
  "do_basic_tokenize": true,
  "do_lower_case": true,
  "mask_token": "[MASK]",
  "model_max_length": 512,
  "never_split": null,
  "pad_token": "[PAD]",
  "sep_token": "[SEP]",
  "strip_accents": null,
  "tokenize_chinese_chars": true,
  "tokenizer_class": "BertTokenizer",
  "unk_token": "[UNK]"
}
```

Intellij 的 tokenizer_config.json 配置：

```json
{
  "do_lower_case": true,
  "unk_token": "[UNK]",
  "sep_token": "[SEP]",
  "pad_token": "[PAD]",
  "cls_token": "[CLS]",
  "mask_token": "[MASK]",
  "clean_up_tokenization_spaces": true,
  "tokenize_chinese_chars": true,
  "strip_accents": null,
  "special_tokens_map_file": null,
  "do_basic_tokenize": true,
  "never_split": null,
  "tokenizer_class": "BertTokenizer",
  "model_max_length": 512
}
```

