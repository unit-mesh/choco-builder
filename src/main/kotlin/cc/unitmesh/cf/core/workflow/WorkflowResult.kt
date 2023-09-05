package cc.unitmesh.cf.core.workflow

class WorkflowResult(
    val currentStage: StageContext.Stage,
    val nextStage: StageContext.Stage,
    val responseMsg: String,
    val resultType: Class<*>,
    val result: Any,
)