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

    override val prompts: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            EXECUTE.stage to EXECUTE
        )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        // TODO clarify user question, 如系统包含了这些规范，你需要哪些规范？
        val specs = relevantSearch.search(chatWebContext.messages.last {
            it.role == LlmMsg.ChatRole.User.value
        }.content)

        val userMsg = EXECUTE.format()
            .replace("${'$'}{specs}", specs.joinToString("\n"))
            .replace("${'$'}{question}", chatWebContext.messages[0].content)

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
            |
            |已有规范信息：
            |
            |```design
            |${'$'}{specs}
            |```
            |用户的问题：
            |${'$'}{question}
            |
            |现在请你根据规范信息，回答用户的问题。
            |
            |""".trimMargin()
        )
    }
}