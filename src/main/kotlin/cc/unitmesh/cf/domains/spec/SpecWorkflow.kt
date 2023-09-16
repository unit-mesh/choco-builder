package cc.unitmesh.cf.domains.spec

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SpecWorkflow : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    @Autowired
    private lateinit var relevantSearch: SpecRelevantSearch

    override val stages: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            EXECUTE.stage to EXECUTE
        )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        val question = chatWebContext.messages.last {
            it.role == LlmMsg.ChatRole.User.value
        }.content

        // TODO clarify user question, 如系统包含了这些规范，你需要哪些规范？
        val specs = relevantSearch.search(question)

        val userMsg = EXECUTE.format()
            .replace("${'$'}{specs}", specs.map {
                "source: ${it.source} content: ${it.content}"
            }.joinToString("\n"))
            .replace("${'$'}{question}", question)

        val flowable = llmProvider.streamCompletion(listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, userMsg),
        ).filter { it.content.isNotBlank() }
        )

        return Flowable.create({ emitter ->
            flowable.subscribe({
                val workflowResult = WorkflowResult(
                    currentStage = StageContext.Stage.Execute,
                    nextStage = StageContext.Stage.Execute,
                    responseMsg = it,
                    resultType = String::class.java.toString(),
                    result = it
                )
                emitter.onNext(workflowResult)
            }, {
                emitter.onError(it)
            }, {
                emitter.onComplete()
            })
        }, BackpressureStrategy.BUFFER)
    }

    companion object {

        val EXECUTE: StageContext = StageContext(
            id = "Spec Execute",
            stage = StageContext.Stage.Execute,
            systemPrompt = """你是一个资深的架构专家，你需要根据用户提供的问题与已有规范信息。
            |
            |- 如果规范缺少对应的信息，你不要回答。
            |- 你必须回答用户的问题。
            |- 请根据客户的问题，返回对应的规范，并返回对应的 source 相关信息。
            |
            |
            |已有规范信息：
            |
            |```design
            |${'$'}{specs}
            |```
            |
            |示例：
            |用户的问题：哪些规范包含了架构设计？
            |回答：
            |###
            |
            |出处：后端代码规范的命名规范章节 // 这里根据规范的 source 项信息，写出对应的来源
            |// 这里，你需要返回是规范中的详细信息，而不是规范的标题。
            |###
            |
            |现在请你根据规范信息，回答用户的问题。
            |用户的问题：
            |${'$'}{question}
            |""".trimMargin()
        )
    }
}