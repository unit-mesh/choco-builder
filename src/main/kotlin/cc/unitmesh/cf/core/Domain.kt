package cc.unitmesh.cf.core

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Domain(
    val name: String,
    val description: String,
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class SubDomain(
    val name: String,
    val description: String,
)

annotation class ToolSchema(
    val name: String,
    val args: Array<String>,
)


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Tool(
    val name: String,
    val description: String,
    val schemas: Array<ToolSchema> = [],
    val examples: Array<String> = [],
)
