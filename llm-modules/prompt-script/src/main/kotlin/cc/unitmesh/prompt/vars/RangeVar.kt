package cc.unitmesh.prompt.vars

sealed class ConnectionVar<T : Comparable<T>>(
    val name: String, // 变量名称
    val type: String, // 变量类型
    val range: ClosedRange<T>, // 变量范围
    val step: T, // 步长
) {
    init {
        require(step > 0) {
            "Invalid step for $name variable. Step should be greater than 0."
        }
    }

    class FloatConnectionVar(
        name: String,
        range: ClosedRange<Float>,
        step: Float,
    ) : ConnectionVar<Float>(name, "float", range, step)

    class IntConnectionVar(
        name: String,
        range: ClosedRange<Int>,
        step: Int,
    ) : ConnectionVar<Int>(name, "int", range, step)
}

private operator fun <T> Comparable<T>.compareTo(i: Int): Int {
    return 0
}
