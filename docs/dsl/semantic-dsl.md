---
layout: default
title: ExplainQuery Semantic DSL
parent: Domain Specific Language
nav_order: 2
permalink: /dsl/semantic-query
---

Semantic DSL 基于 [CoUnit](https://github.com/unit-mesh/co-unit) 中的 DSL 所创建的：

![CoUnit Prompt 策略](https://github.com/unit-mesh/co-unit/raw/master/docs/counit-prompt-strategy.svg)

CoUnit 对应的参考来源为： 

- Bloop：[https://github.com/BloopAI/bloop](https://github.com/BloopAI/bloop)
- HyDE doc：[Precise Zero-Shot Dense Retrieval without Relevance Labels](https://arxiv.org/abs/2212.10496)

示例代码如下：

```markdown
englishQuery: query git path change count by system id
originLanguageQuery: 通过 ID 查找 Git 代码库路径变更统计信息
hypotheticalCode:
###kotlin
@SqlQuery(
  "select system_id as systemId, line_count as lineCount, path, changes" +
  " from scm_path_change_count where system_id = :systemId"
)
fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
###
```

