package cc.unitmesh.cf.core.context.variable

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Variable(
    // 变量名
    val name: String
)
