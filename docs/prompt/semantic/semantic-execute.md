---
layout: default
title: Semantic 搜索：Execute 阶段
parent: Prompt logs
nav_order: 32
---

## Input

Your job is to answer a query about a codebase using the information above.

You must use the following formatting rules at all times:

- Provide only as much information and code as is necessary to answer the query and be concise
- If you do not have enough information needed to answer the query, do not make up an answer
- When referring to code, you must provide an example in a code block
- Keep number of quoted lines of code to a minimum when possible
- Basic markdown is allowed

相关的代码：

```kotlin
// canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticDecl
override fun workflow(question: String): Workflow {
    return CodeSemanticWorkflow()
}    
```

```kotlin
// canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
    val question = chatWebContext.messages.last().content
    val analyze = SemanticProblemAnalyzer(llmProvider)
        .analyze(domainName, question)
    // todo: send to local
    log.info("Semantic analyze: {}", analyze)
    val answerFlowable: Flowable<Answer> =
        SemanticSolutionExecutor(llmProvider, store, embedding, variableResolver).execute(analyze)

    return toFlowableResult(answerFlowable)
}
```

```kotlin
// canonicalName: cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
    val answer: Flowable<Answer> = CodeSolutionExecutor(llmProvider, codeInterpreter).execute(
        CodeInput(content = chatWebContext.messages.last().content)
    )

    return Flowable.create({ emitter ->
        answer.subscribe({
            val workflowResult = WorkflowResult(
                currentStage = StageContext.Stage.Execute,
                nextStage = StageContext.Stage.Execute,
                responseMsg = it.values.toString(),
                resultType = String::class.java.toString(),
                result = it.values.toString()
            )
            emitter.onNext(workflowResult)
        }, {
            emitter.onError(it)
        }, {
            emitter.onComplete()
        })
    }, io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER)
}
```

用户的问题：Semantic Workflow 是如何实现的？

## Output

Semantic Workflow 是通过 `CodeSemanticWorkflow` 类来实现的。在 `CodeSemanticWorkflow` 类中，有一个 `execute` 方法，
该方法接收两个参数 `prompt` 和 `chatWebContext`，并返回一个 `Flowable<WorkflowResult>` 对象。

在 `execute` 方法中，首先获取最后一条消息的内容作为问题。然后使用 `SemanticProblemAnalyzer` 对象对问题进行分析，
并将分析结果赋值给 `analyze` 变量。接下来，使用 `SemanticSolutionExecutor`
对象执行分析结果，并将执行结果赋值给 `answerFlowable` 变量。

最后，将 `answerFlowable` 转换为 `Flowable<WorkflowResult>` 对象并返回。

以下是相关代码片段：

```kotlin
// canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
    val question = chatWebContext.messages.last().content
    val analyze = SemanticProblemAnalyzer(llmProvider).analyze(domainName, question)
    log.info("Semantic analyze: {}", analyze)
    val answerFlowable: Flowable<Answer> =
        SemanticSolutionExecutor(llmProvider, store, embedding, variableResolver).execute(analyze)
    return toFlowableResult(answerFlowable)
}
```
