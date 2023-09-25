---
layout: default
title: 前瞻性主动检索增强生成：FLARE
parent: Patterns
nav_order: 2
---

前瞻性主动检索增强生成（FLARE）

![FLARE 过程示例](https://github.com/jzbjyb/FLARE/raw/main/res/flare.gif)

> FLARE（Forward-Looking Active REtrieval augmented generation） 是一种通用的检索增强生成方法，它通过对即将到来的句子进行预测来主动决定何时以及何时检索，
> 以预测未来内容，并将其用作查询以检索相关文档，如果句子包含置信度较低的标记。


FLARE（Forward-Looking Active REtrieval augmented generation）的主要目标是提高自然语言处理模型的生成能力，使其能够在生成文本时主动获取和利用外部信息，以提供更准确、全面和信息丰富的答案。以下是FLARE的一些主要目标和优势：

1. **主动检索和利用外部信息：** FLARE可以主动识别模型不确定的标记，并自动检索相关文档，以获取有关这些标记的信息。这有助于模型获取丰富的上下文信息，从而提高了生成文本的质量和准确性。
2. **提高生成文本的一致性：** FLARE可以帮助生成模型避免生成不一致或错误的信息，因为它会在需要时检索准确的信息来支持文本生成。
3. **扩展模型的知识和能力：** FLARE使模型能够利用外部知识库、文档或资源，从而扩展了模型的知识和能力，使其能够回答更广泛的问题。
4. **提高生成文本的可解释性：** FLARE可以使模型的生成过程更具可解释性，因为它明确地显示了模型何时以及为什么选择检索外部信息。这有助于用户理解模型的工作方式。
5. **应对模型的不确定性：** FLARE有助于处理模型在生成文本时的不确定性，因为它可以在模型不确定的情况下及时获取支持性信息，从而提高了生成的可靠性。

总之，FLARE旨在增强生成模型的能力，使其更智能地获取和利用外部信息，从而提高生成文本的质量、准确性和可用性。这对于各种自然语言处理任务和应用程序都具有潜在的益处，如问答系统、文本摘要、翻译等。

## 相关资源

LangChain 示例：[Retrieve as you generate with FLARE](https://python.langchain.com/docs/use_cases/question_answering/how_to/flare)


