---
layout: default
title: Semantic 搜索：Analyze 阶段
parent: Prompt Strategy
nav_order: 31
---

## Input


Your are a senior software developer, your job is to transpile user's question relative to codebase.

1. YOU MUST follow the DSL format.
2. You MUST translate user's question into a DSL query.
3. `englishQuery` is a reference to the document that you think is the answer to the question.
4. `originLanguageQuery` 是从用户的问题中提取出来的自然语言查询，以用于查询用户的问题。
5. `hypotheticalDocument` ia a code snippet that could hypothetically be returned by a code search engine as the answer.
5.

For examples:


Q:如何通过 ID 查找代码库变更信息?
A:
englishQuery: query git path change count by system id
originLanguageQuery: 通过 ID 查找 Git 代码库路径变更统计信息
hypotheticalDocument: 
```kotlin
@SqlQuery(
  "select system_id as systemId, line_count as lineCount, path, changes" +
  " from scm_path_change_count where system_id = :systemId"
)
fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
```

###

Q:What's the Qdrant threshold?
A:
- englishQuery: Qdrant threshold (point, score, offset)
- originLanguageQuery: Qdrant 阈值 (point, score, offset)
- hypotheticalDocument:

```rust
SearchPoints {{
   limit,
   vector: vectors.get(idx).unwrap().clone(),
   collection_name: COLLECTION_NAME.to_string(),
   offset: Some(offset),
   score_threshold: Some(0.3),
   with_payload: Some(WithPayloadSelector {{
   selector_options: Some(with_payload_selector::SelectorOptions::Enable(true)),
   }})
```

## Output

- englishQuery: How is Semantic Workflow implemented?
- originLanguageQuery: Semantic Workflow 是如何实现的？
- hypotheticalDocument:

```java
public class SemanticWorkflow {
    private WorkflowEngine workflowEngine;

    public SemanticWorkflow() {
        this.workflowEngine = new WorkflowEngine();
    }

    public void executeWorkflow() {
// Implement the logic for executing the Semantic Workflow here
    }

    public void addWorkflowStep(WorkflowStep step) {
        workflowEngine.addStep(step);
    }

    public void removeWorkflowStep(WorkflowStep step) {
        workflowEngine.removeStep(step);
    }

    public List<WorkflowStep> getWorkflowSteps() {
        return workflowEngine.getSteps();
    }
}
``` 