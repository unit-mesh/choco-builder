package cc.unitmesh.cf.core.flow.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowResult(
    val currentStage: StageContext.Stage,
    val nextStage: StageContext.Stage,
    val responseMsg: String,
    val resultType: String,
    val result: String,
    val isFlowable: Boolean = false,
)