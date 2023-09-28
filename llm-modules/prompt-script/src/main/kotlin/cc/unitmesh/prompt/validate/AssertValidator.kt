package cc.unitmesh.prompt.validate

import java.util.regex.Pattern


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

enum class CompareType {
    ENDS_WITH,
    STARTS_WITH,
    CONTAINS,
    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    GREATER_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN_OR_EQUAL,
}

data class ComparisonExpression(
    var left: String,
    val compareType: CompareType,
    val right: String,
    var equal: Boolean = true,
) : Expression() {
    override fun evaluate(output: String): Boolean {
        val result = when (compareType) {
            CompareType.ENDS_WITH -> {
                left.endsWith(right)
            }

            CompareType.STARTS_WITH -> {
                left.startsWith(right)
            }

            CompareType.CONTAINS -> {
                left.contains(right)
            }

            CompareType.EQUAL -> {
                try {
                    right.toDouble() == left.toDouble()
                } catch (e: Exception) {
                    right == left
                }
            }

            CompareType.NOT_EQUAL -> {
                right.toDouble() != left.toDouble()
            }

            CompareType.LESS_THAN -> {
                left.toDouble() < right.toDouble()
            }

            CompareType.GREATER_THAN -> {
                left.toDouble() > right.toDouble()
            }

            CompareType.LESS_THAN_OR_EQUAL -> {
                left.toDouble() <= right.toDouble()
            }

            CompareType.GREATER_THAN_OR_EQUAL -> {
                left.toDouble() >= right.toDouble()
            }
        }

        return if (equal) result else !result
    }
}


/**
 * A assert expression will be evaluated to a boolean value. And it will be used to determine whether the following
 * statements should be executed.
 *
 * @type the assert expression type, like equals, contains, etc.
 * @left the left value of the expression
 * @right the right value of the expression
 * @negate whether the expression should be negated
 */
class AssertExpr {

    fun tokenizeExpression(expression: String): List<String> {
        // 使用正则表达式将表达式字符串拆分为标记（tokens）
        val pattern = Pattern.compile("""(\|\||&&|\(|\)|!=|!|<=|>=|==|<|>|[a-zA-Z]+|\d+)""")
        val matcher = pattern.matcher(expression)
        val tokens = mutableListOf<String>()

        while (matcher.find()) {
            tokens.add(matcher.group())
        }

        return tokens
    }

    // 在这里实现递归解析表达式的代码
    // 你可以使用递归下降解析器或其他方法来实现解析
    fun parseExpression(tokens: MutableList<String>): Expression {
        return parseOrExpression(tokens)
    }

    fun parseOrExpression(tokens: MutableList<String>): Expression {
        var left = parseAndExpression(tokens)

        while (tokens.isNotEmpty() && tokens[0] == "||") {
            tokens.removeAt(0)
            val right = parseAndExpression(tokens)
            left = OrExpression(left, right)
        }

        return left
    }

    fun parseAndExpression(tokens: MutableList<String>): Expression {
        var left = parseComparisonExpression(tokens)

        while (tokens.isNotEmpty() && tokens[0] == "&&") {
            tokens.removeAt(0)
            val right = parseComparisonExpression(tokens)
            left = AndExpression(left, right)
        }

        return left
    }

    fun parseComparisonExpression(tokens: MutableList<String>): Expression {
        if (tokens.isNotEmpty() && tokens[0] == "!") {
            tokens.removeAt(0)
            val expression = parseComparisonExpression(tokens)
            return NotExpression(expression)
        }

        val leftOperand = tokens.removeAt(0)
        val operator = tokens.removeAt(0)
        val rightOperand = tokens.removeAt(0)

        val compareType = when (operator) {
            "==" -> CompareType.EQUAL
            "!=" -> CompareType.NOT_EQUAL
            "&&" -> CompareType.EQUAL
            "contains" -> CompareType.CONTAINS
            "startsWith" -> CompareType.STARTS_WITH
            "endsWith" -> CompareType.ENDS_WITH
            "<" -> CompareType.LESS_THAN
            ">" -> CompareType.GREATER_THAN
            "<=" -> CompareType.LESS_THAN_OR_EQUAL
            ">=" -> CompareType.GREATER_THAN_OR_EQUAL
            else -> throw IllegalArgumentException("Unsupported operator: $operator")
        }

        return ComparisonExpression(leftOperand, compareType, rightOperand)
    }

    companion object {
        fun eval(output: String, expression: String): Boolean {
            val assertExpr = AssertExpr()
            val tokens = assertExpr.tokenizeExpression(expression).toMutableList()
            if (tokens[0] == "output") {
                tokens[0] = output
            }

            val parsedExpression = assertExpr.parseExpression(tokens.toMutableList())

            return when (parsedExpression) {
                is AndExpression -> parsedExpression.left.evaluate(output) && parsedExpression.right.evaluate(output)
                is OrExpression -> parsedExpression.left.evaluate(output) || parsedExpression.right.evaluate(output)
                is NotExpression -> !parsedExpression.expression.evaluate(output)
                is ComparisonExpression -> parsedExpression.evaluate(output)
            }
        }
    }
}
