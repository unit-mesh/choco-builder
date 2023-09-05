package cc.unitmesh.cf.core.workflow

import kotlinx.serialization.Serializable

@Serializable
class WorkflowResult(
    val currentStage: StageContext.Stage,
    val nextStage: StageContext.Stage,
    val responseMsg: String,
    val resultType: String,
    val result: String,
)