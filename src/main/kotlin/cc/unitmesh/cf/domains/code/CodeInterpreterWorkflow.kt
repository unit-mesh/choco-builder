package cc.unitmesh.cf.domains.code

import cc.unitmesh.cf.core.prompt.QAExample
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.domains.code.flow.CodeInput
import cc.unitmesh.cf.domains.code.flow.CodeSolutionExecutor
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import io.reactivex.rxjava3.core.Flowable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CodeInterpreterWorkflow : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    @Autowired
    private lateinit var codeInterpreter: CodeInterpreter

    override val prompts: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            StageContext.Stage.Execute to EXECUTE
        )

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
                |你要尽可能优化函数的输出方式，以便于将函数的输出将会被发送给用户.
                | 
                """.trimMargin(),
            examples = listOf(
                QAExample(
                    question = "\n" +
                            "使用图表展示 2023 年上半年电费信息：\n" +
                            "\n" +
                            "| 月份 | 支出    |\n" +
                            "|----|-------|\n" +
                            "| 一月 | 201.2 |\n" +
                            "| 二月 | 222   |\n" +
                            "| 三月 | 234.3 |\n" +
                            "| 四月 | 120.2 |\n" +
                            "| 五月 | 90    |\n" +
                            "| 六月 | 94.4  |",
                    answer = """```kotlin
                        |%use lets-plot
                        |import kotlin.math.PI
                        |import kotlin.random.Random
                        |
                        |val incomeData = mapOf(
                        |   "x" to listOf("一月", "二月", "三月", "四月", "五月", "六月"),
                        |   "y" to listOf(201.2, 222, 234.3, 120.2, 90, 94.4)
                        |)
                        |
                        |letsPlot(incomeData) { x = "x"; y = "y" } +
                        |   geomBar(stat = Stat.identity) +
                        |   geomText(labelFormat = "\${'$'}{.2f}") { label = "y"; } +
                        |   ggtitle("2023 年上半年电费")
                        |```
                    """.trimMargin()
                ),
                QAExample(
                    question = "编写一个 9x9 乘法表",
                    answer = """```kotlin
                        |fun printMultiplicationTable(): String {
                        |   val sb = StringBuilder()
                        |   for (i in 1..9) {
                        |       for (j in 1..9) {
                        |           sb.append("${'$'}{i * j}\t")
                        |       }
                        |       sb.append("\n")
                        |   }
                        |   return sb.toString()
                        |}
                        |
                        |printMultiplicationTable()
                        |```
                    """.trimMargin()
                )
            )
        )
    }
}
