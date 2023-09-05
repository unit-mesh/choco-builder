package cc.unitmesh.cf.core.workflow

class WorkflowResult(
    val stage: StageContext.Stage,
    val resultType: Class<*>,
    val result: Any,
)