package cc.unitmesh.prompt.assert

import java.util.regex.Pattern

/**
 * A assert expression will be evaluated to a boolean value. And it will be used to determine whether the following
 * statements should be executed.
 *
 * @type the assert expression type, like equals, contains, etc.
 * @left the left value of the expression
 * @right the right value of the expression
 * @negate whether the expression should be negated
 */
data class AssertExpr(
    val type: String,
    val left: String,
    val right: String,
    val negate: Boolean,
) {
    fun evaluateExpression(output: String, expression: String): Boolean {
        val tokens = tokenizeExpression(expression)
        val parsedExpression = parseExpression(tokens)

        return when (parsedExpression) {
            is AndExpression -> parsedExpression.left.evaluate(output) && parsedExpression.right.evaluate(output)
            is OrExpression -> parsedExpression.left.evaluate(output) || parsedExpression.right.evaluate(output)
            is NotExpression -> !parsedExpression.expression.evaluate(output)
            is ComparisonExpression -> parsedExpression.evaluate(output)
            else -> throw IllegalArgumentException("Invalid expression: $expression")
        }
    }

    sealed class Expression {
        abstract fun evaluate(output: String): Boolean
    }

    data class AndExpression(val left: Expression, val right: Expression) : Expression() {
        override fun evaluate(output: String): Boolean {
            return left.evaluate(output) && right.evaluate(output)
        }
    }

    data class OrExpression(val left: Expression, val right: Expression) : Expression() {
        override fun evaluate(output: String): Boolean {
            return left.evaluate(output) || right.evaluate(output)
        }
    }

    data class NotExpression(val expression: Expression) : Expression() {
        override fun evaluate(output: String): Boolean {
            return !expression.evaluate(output)
        }
    }

    data class ComparisonExpression(val left: String, val operator: String, val right: String) : Expression() {
        override fun evaluate(output: String): Boolean {
            return when (operator) {
                "==" -> output == right
                "!=" -> output != right
                "<" -> output.toInt() < right.toInt()
                ">" -> output.toInt() > right.toInt()
                "<=" -> output.toInt() <= right.toInt()
                ">=" -> output.toInt() >= right.toInt()
                else -> throw IllegalArgumentException("Invalid operator: $operator")
            }
        }
    }

    fun tokenizeExpression(expression: String): List<String> {
        // 使用正则表达式将表达式字符串拆分为标记（tokens）
        val pattern = Pattern.compile("""(\|\||&&|\(|\)|!|==|!=|<|>|[a-zA-Z]+|\d+)""")
        val matcher = pattern.matcher(expression)
        val tokens = mutableListOf<String>()

        while (matcher.find()) {
            tokens.add(matcher.group())
        }

        return tokens
    }

    fun parseExpression(tokens: List<String>): Expression {
        // 在这里实现递归解析表达式的代码
        // 你可以使用递归下降解析器或其他方法来实现解析
        // 这里的示例是一个简单的实现
        TODO()
    }

}