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
 * An assert expression will be evaluated to a boolean value. And it will be used to determine whether the following
 * statements should be executed.
 *
 * @type the assert expression type, like equals, contains, etc.
 * @left the left value of the expression
 * @right the right value of the expression
 * @negate whether the expression should be negated
 */
class StringValidator(val expression: String, override val input: String) : Validator {
    override fun validate(): Boolean {
        val tokens = tokenizeExpression(expression).toMutableList()
        if (tokens[0] == "output") {
            tokens[0] = input
        }

        return when (val parsedExpression = parseExpression(tokens.toMutableList())) {
            is AndExpression -> parsedExpression.left.evaluate(input) && parsedExpression.right.evaluate(input)
            is OrExpression -> parsedExpression.left.evaluate(input) || parsedExpression.right.evaluate(input)
            is NotExpression -> !parsedExpression.expression.evaluate(input)
            is ComparisonExpression -> parsedExpression.evaluate(input)
        }
    }

    /**
     * Should convert an expression string to three tokens
     */
    private fun tokenizeExpression(expression: String): List<String> {
        val pattern = Pattern.compile("""(\|\||&&|\(|\)|!=|!|<=|>=|==|<|>|[a-zA-Z]+|\d+)""")
        val matcher = pattern.matcher(expression)
        val tokens = mutableListOf<String>()

        // Loop through the matches and add them to tokens
        while (matcher.find()) {
            tokens.add(matcher.group())

            // Check if we have collected three tokens
            if (tokens.size == 2) {
                break // Exit the loop once we have three tokens
            }
        }

        // If we only have two tokens, join the remaining string into the third token
        if (tokens.size == 2) {
            val remainingString = expression.substring(matcher.end())
            tokens.add(remainingString.trim().removeSurrounding("\""))
        }

        // Ensure tokens contains exactly three elements
        if (tokens.size != 3) {
            // Handle the case where there are not enough tokens
            throw IllegalArgumentException("Invalid expression: $expression")
        }

        return tokens
    }


    // 在这里实现递归解析表达式的代码
    // 你可以使用递归下降解析器或其他方法来实现解析
    private fun parseExpression(tokens: MutableList<String>): Expression {
        return parseOrExpression(tokens)
    }

    private fun parseOrExpression(tokens: MutableList<String>): Expression {
        var left = parseAndExpression(tokens)

        while (tokens.isNotEmpty() && tokens[0] == "||") {
            tokens.removeAt(0)
            val right = parseAndExpression(tokens)
            left = OrExpression(left, right)
        }

        return left
    }

    private fun parseAndExpression(tokens: MutableList<String>): Expression {
        var left = parseComparisonExpression(tokens)

        while (tokens.isNotEmpty() && tokens[0] == "&&") {
            tokens.removeAt(0)
            val right = parseComparisonExpression(tokens)
            left = AndExpression(left, right)
        }

        return left
    }

    private fun parseComparisonExpression(tokens: MutableList<String>): Expression {
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
}
