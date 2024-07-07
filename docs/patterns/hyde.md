---
layout: default
title: HyDE 提升文档检索
parent: Patterns
nav_order: 2
---

![HyDE Approach](https://github.com/texttron/hyde/raw/main/approach.png)

> HyDE （Hypothetical Document Embeddings）是一种搜索技术，它通过生成虚构文档，并将其转化成向量，来帮助搜索系统在没有相关性标签的情况下
> 找到相关信息。 它能够过滤掉虚构文档中的错误信息，具有强大的搜索性能，适用于多种任务和语言。

案例：[https://github.com/BloopAI/bloop](https://github.com/BloopAI/bloop) 使用 HyDE 生成假设性代码，以进行代码的相似式检索。

## HyDE 实现

HyDE 由三个步骤组成：

1. 生成假设文档：使用 zero-shot 方法，指导一个指令驱动的语言模型（如 InstructGPT）生成一个假设性文档。这个文档捕获了相关性模式，尽管它是虚构的，并且可能包含错误细节。
2. 对比编码器：接着，通过一个无监督对比学习的编码器（如 Contriever），将生成的假设文档编码成一个嵌入向量。这个向量在语料库嵌入空间中确定了一个邻域，基于向量相似性从中检索出相似的真实文档。
3. 关联实际语料库搜索：第二步将生成的文档与实际语料库进行关联，将生成的向量在本地知识库中进行相似性检索，寻找最终结果。

即，根据用户的输入，生成一个假设性的文档，然后将其转换成向量，最后在本地知识库中进行相似性检索。

## 示例

ChocoBuilder 实现示例：

```kotlin
ExplainQuery(
    question = "如何通过 ID 查找代码库变更信息?",
    englishQuery = "query git path change count by system id",
    originLanguageQuery = "// 通过 id 查找 Git 代码库路径变更统计信息",
    hypotheticalCode = """
        |```kotlin
        |   @SqlQuery(
        |       "select system_id as systemId, line_count as lineCount, path, changes" +
        |       " from scm_path_change_count where system_id = :systemId"
        |   )
        |   fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
        |```""".trimMargin()
)
```

## 相关资源


GitHub: [https://github.com/texttron/hyde](https://github.com/texttron/hyde)
原始论文信息：[Precise Zero-Shot Dense Retrieval without Relevance Labels](https://arxiv.org/abs/2212.10496)

其它:

[Improve document indexing with HyDE](https://python.langchain.com/docs/use_cases/question_answering/how_to/hyde)