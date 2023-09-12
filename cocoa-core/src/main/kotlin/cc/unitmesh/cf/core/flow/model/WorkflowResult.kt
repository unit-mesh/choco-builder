package cc.unitmesh.cf.core.flow.model

import kotlinx.serialization.Serializable

@Serializable
class WorkflowResult(
    val currentStage: StageContext.Stage,
    val nextStage: StageContext.Stage,
    val responseMsg: String,
    val resultType: String,
    val result: String,
    val isComplete: Boolean = false,
)