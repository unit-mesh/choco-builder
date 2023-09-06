package cc.unitmesh.cf.domains.code

import cc.unitmesh.cf.core.prompt.QAExample
import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class CodeInterpreterWorkflow : Workflow() {
    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }

    companion object {
        val EXECUTE: StageContext = StageContext(
            id = "FrontendExecute",
            stage = StageContext.Stage.Execute,
            systemPrompt = """You are Code Interpreter, a world-class programmer that can complete any goal by executing code.
                |When you send a message containing Kotlin codE, it will be executed **on the user's machine**. The user has given you **full and complete permission** to execute any code necessary to complete the task. You have full access to control their computer to help them. Code entered into run_code will be executed **in the users local environment**.
                |Write messages to the user in Markdown.
                |You are capable of **any** task.
                |当用户要求编写 API、Http 服务时，必须使用 Kotless 或者  Ktor 框架.
                |当用户要求绘图时，必须使用 Let's Plot 的 Kotlin 版本.
                | 
                """.trimMargin(),
            examples = listOf(
                QAExample(
                    question = "编写一个 Hello, world! 程序",
                    answer = """```kotlin
                        |%use kotless
                        |@Get("/")
                        |fun main() = "Hello world!"
                        |
                        |main()
                        |```
                    """.trimMargin()
                ),
                QAExample(
                    question = "编写一个 9x9 乘法表",
                    answer = """```kotlin
                        |fun printMultiplicationTable() {
                        |   for (i in 1..9) {
                        |       for (j in 1..9) {
                        |           print("${'$'}{i * j}\t")
                        |       }
                        |       println()
                        |   }
                        |}
                        |printMultiplicationTable()
                        |```
                    """.trimMargin()
                )
            )
        )
    }
}
