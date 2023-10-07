---
layout: default
title: UI 设计 - 总览
parent: Prompt Sample
nav_order: 0
---

PS：以下内容由 Code Semantic Workflow 生成。

根据提供的代码，FEWorkflow 是一个抽象类，它有三个阶段：Clarify、Design 和 Execute。每个阶段都有对应的执行逻辑。

在 FEWorkflow 的子类 FEProblemClarifier 中，Clarify 阶段的执行逻辑如下：

- 根据传入的问题和历史消息，生成一系列的聊天消息。
- 调用 LlmProvider 的 completion 方法，将聊天消息作为参数，获取一个 FlowActionFlag 对象。
- 根据 FlowActionFlag 的值，确定下一个阶段是 Clarify 还是 Design。
- 如果下一个阶段是 Design，则将生成的回答消息添加到 chatWebContext 的 messages 中，并调用 execute 方法进入 Design 阶段。
- 如果下一个阶段是 Clarify，则返回当前阶段的 WorkflowResult 对象。

在 FEWorkflow 的子类 FEWorkflow.execute 中，Design 阶段的执行逻辑如下：

- 获取 chatWebContext 的 messages。
- 根据 chatWebContext 的 stage 和最后一条消息的内容判断当前阶段是否需要进入 Execute 阶段。
- 如果需要进入 Execute 阶段，则将 stage 设置为 Execute。
- 根据当前阶段的值，执行相应的逻辑。
- 返回对应阶段的 WorkflowResult 对象。

在 FEWorkflow 的子类 FESolutionExecutor.execute 中，Execute 阶段的执行逻辑如下：

- 根据传入的 UiPage 对象，获取相关的信息并存储到 variable 中。
- 准备相关的组件并存储到 userComponents 中。
- 根据 basePrompt 和 variable 的值，生成一个 LlmMsg.ChatMessage 对象的列表。
- 过滤掉内容为空的 LlmMsg.ChatMessage 对象。
- 调用 completion 的 streamCompletion 方法，将 LlmMsg.ChatMessage 列表作为参数，获取一个 Flowable<String> 对象。
- 创建一个 Flowable<Answer> 对象，通过 completion 的结果创建 Answer 对象，并将其发送给订阅者。
- 返回 Flowable<Answer> 对象。
