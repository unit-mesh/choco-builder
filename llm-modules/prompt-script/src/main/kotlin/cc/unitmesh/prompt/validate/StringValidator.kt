package cc.unitmesh.prompt.validate

data class CompareExpr(val left: String, val operator: String, val right: String)

/**
 * This class represents a string validation expression that evaluates to a boolean value, determining whether subsequent
 * statements should be executed or not.
 *
 * @param expression The string validation expression, which can include functions like "contains," "endsWith," and others.
 * @param input The input string against which the expression is evaluated to generate a result.
 *
 * Example usage:
 * ```kotlin
 * StringValidator("output contains \"hello\"", "hello world").validate() shouldBe true
 * StringValidator("output endsWith \"world\"", "hello world").validate() shouldBe true
 * StringValidator("output startsWith \"hello\"", "hello world").validate() shouldBe true
 * StringValidator("output == \"hello world\"", "hello world").validate() shouldBe true
 * StringValidator("output.length > 5", "hello, world").validate() shouldBe true
 * ```
 */
class StringValidator(val expression: String, val input: String) {
    fun validate(): Boolean {
        // tokenize string and convert to a Compare data class (left = input, type, right)
        val compareExpr = parseExpression(expression)
        val left = if (compareExpr.left == "output") input else compareExpr.left
        return when (compareExpr.operator) {
            "==" -> left == compareExpr.right
            "!=" -> left != compareExpr.right
            "contains" -> left.contains(compareExpr.right)
            "startsWith" -> left.startsWith(compareExpr.right)
            "endsWith" -> left.endsWith(compareExpr.right)
            ">" -> {
                val lengthComparison = compareExpr.right.toIntOrNull() ?: throw IllegalArgumentException("Invalid length comparison value: ${compareExpr.right}")
                left.length > lengthComparison
            }
            "<" -> {
                val lengthComparison = compareExpr.right.toIntOrNull() ?: throw IllegalArgumentException("Invalid length comparison value: ${compareExpr.right}")
                left.length < lengthComparison
            }
            else -> throw IllegalArgumentException("Unsupported operator: ${compareExpr.operator}")
        }
    }

    private fun parseExpression(expression: String): CompareExpr {
        val length = expression.length
        var currentIndex = 0

        // 解析左操作数
        val leftBuilder = StringBuilder()
        while (currentIndex < length && !expression[currentIndex].isWhitespace()) {
            leftBuilder.append(expression[currentIndex])
            currentIndex++
        }
        val left = leftBuilder.toString()

        // 跳过空格和点号
        while (currentIndex < length && (expression[currentIndex].isWhitespace())) {
            currentIndex++
        }

        // 解析操作符
        val operatorBuilder = StringBuilder()
        while (currentIndex < length && !expression[currentIndex].isWhitespace()) {
            operatorBuilder.append(expression[currentIndex])
            currentIndex++
        }
        val operator = operatorBuilder.toString()

        // 跳过空格
        while (currentIndex < length && expression[currentIndex].isWhitespace()) {
            currentIndex++
        }

        // 解析右操作数
        val rightBuilder = StringBuilder()
        while (currentIndex < length) {
            rightBuilder.append(expression[currentIndex])
            currentIndex++
        }
        val right = rightBuilder.toString().trim('\"')

        return CompareExpr(left, operator, right)
    }
}
