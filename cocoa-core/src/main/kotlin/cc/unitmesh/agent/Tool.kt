package cc.unitmesh.agent


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CLASS
)
annotation class Tool(
    val name: String = "",
    vararg val value: String = [""],
)


