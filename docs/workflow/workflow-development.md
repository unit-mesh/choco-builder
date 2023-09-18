---
layout: default
title: Domain Workflow
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

## 3. 定义领域特定语言（可选）

我们建议为每个领域定义一个特定的语言，以便于用户更好地理解和使用。例如，我们为前端领域定义了一种特定的语言，如下所示：

```design
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
```

对应的 DSL 代码如下：

```kotlin
@Serializable
data class UiPage(
    override val id: String = IdUtil.uuid(),

    @get:JsonGetter("名称")
    override val name: String,

    @get:JsonGetter("布局")
    val layout: String,

    @get:JsonGetter("内容")
    override val content: String = "",

    @get:JsonGetter("组件")
    val components: List<String> = listOf(),
) : Dsl, IndexElement {
    //...  
}
```

## 4. 创建执行器



```kotlin
interface SolutionExecutor<in T: Dsl> {
    val interpreters: List<Interpreter>
    fun execute(solution: T): Flowable<Answer>
}
```
