package cc.unitmesh.cf.core.workflow

import cc.unitmesh.cf.core.prompt.PromptWithStage

class WorkflowResult(
    val stage: PromptWithStage.Stage,
    val resultType: Class<*>,
    val result: Any,
)