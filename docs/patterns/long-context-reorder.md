---
layout: default
title: 长上下文优化，提升检索质量
parent: Patterns
nav_order: 3
---

## 模式：Lost in the Middle

论文：[https://arxiv.org/abs/2307.03172](https://arxiv.org/abs/2307.03172)

> 当相关信息出现在输入上下文的开头或结尾时，性能通常最高，但当模型必须在长上下文的中间访问相关信息时，性能显著下降。
> 此外，即使对于明确设计为长上下文的模型，随着输入上下文的增长，性能也会显著下降。

即，当模型必须在长上下文的中间访问相关信息时，它们往往会忽略提供的文档。 无论您的模型架构如何，当您包括 10 个以上的检索文档时，性能都会显著下降。

### 排序结果示例

```bash
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

### 相关资源

LangChain 文档：[Lost in the middle: The problem with long contexts](https://python.langchain.com/docs/modules/data_connection/document_transformers/post_retrieval/long_context_reorder)

