package cc.unitmesh.cf.core

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Tool(
    val name: String,
    val description: String,
    val toolSchemas: Array<ToolSchema> = [],
    val examples: Array<String> = [],
)

annotation class ToolSchema(
    val name: String,
    val args: Array<String>,
)