package cc.unitmesh.apply

import cc.unitmesh.apply.base.ApplyDsl

/**
 * Apply is a DSL for invoking a function in a template.
 */
@ApplyDsl
class Workflow(val name: String, private val defaultActor: String) {

}

fun apply(name: String, defaultActor: String = "", init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name, defaultActor)
    workflow.init()
    return workflow
}
