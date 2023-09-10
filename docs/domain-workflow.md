---
layout: default
title: Workflow
nav_order: 3
permalink: /workflow
---

创建新 domain

## 1. 声明 Domain

Chocolate Factory 通过反射来加载 Domain，所以需要在 `cc.unitmesh.cf.domains` 目录下声明 Domain。诸如于：

```kotlin
@Component
class FEDomainDecl : DomainDeclaration {
    override val domainName: String get() = "frontend"
    override val description: String get() = "设计前端 UI，生成前端代码、组件等"

    override fun workflow(question: String): Workflow {
        return FEWorkflow()
    }
}
```

## 2. 创建 Workflow

需要定义一个 Workflow，继承 `cc.unitmesh.cf.workflow.Workflow`，并实现 `prompts` 和 `execute` 方法。

```kotlin
@Component
class FEWorkflow() : Workflow() {
    override val prompts: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            CLARIFY.stage to CLARIFY,
            DESIGN.stage to DESIGN,
            EXECUTE.stage to EXECUTE
        )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        // ...
    }
}
```