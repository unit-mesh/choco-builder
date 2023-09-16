package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import io.reactivex.rxjava3.core.Flowable

abstract class Workflow {
    /**
     * ChatWebContext is a context from GUI
     */
    val chatWebContext: ChatWebContext? = null

    /**
     * provider prompt list for debug in GUI
     * 提供给 GUI 的 workflow 信息，用于调试
     */
    open val stages: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf()

    /**
     * execute the stages of workflow
     */
    abstract fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult>

    fun toFlowableResult(answerFlowable: Flowable<Answer>): Flowable<WorkflowResult> {
        return Flowable.create({ emitter ->
            answerFlowable.subscribe({
                val result = WorkflowResult(
                    currentStage = StageContext.Stage.Execute,
                    nextStage = StageContext.Stage.Done,
                    responseMsg = it.values.toString(),
                    resultType = String::class.java.toString(),
                    result = "",
                    isFlowable = true,
                )

                emitter.onNext(result)
            }, {
                emitter.onError(it)
            }, {
                emitter.onComplete()
            })
        }, io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER)
    }
}