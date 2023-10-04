package cc.unitmesh.prompt.validate

/**
 * This class represents a string validation expression that evaluates to a boolean value, determining whether subsequent
 * statements should be executed or not.
 */
class StringValidator(val expression: String, override val input: String) : Validator {
    override fun validate(): Boolean {
        val expr = parseExpression(expression)
        val left = expr.left
        val right = expr.right
        val operator = expr.operator

        return when (operator) {
            CompareType.EQUALS -> input == right
            CompareType.NOT_EQUALS -> input != right
            CompareType.CONTAINS -> input.contains(right)
            CompareType.STARTS_WITH -> input.startsWith(right)
            CompareType.ENDS_WITH -> input.endsWith(right)
            CompareType.GREATER_THAN -> input.length > right.toInt()
            CompareType.LESS_THAN -> input.length < right.toInt()
            CompareType.PROPERTY_ACCESS -> {
                val propertyAccess = parsePropertyAccess(left)
                when (propertyAccess.operator) {
                    AccessType.LENGTH -> input.length == right.toInt()
                    AccessType.UPPER_CASE -> input.uppercase() == right
                    AccessType.LOWER_CASE -> input.lowercase() == right
                }
            }
        }
    }

    private fun parseExpression(expression: String): CompareExpr {
        // split expression to char and convert
        val chars = expression.toCharArray()
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var isInQuote = false
        for (c in chars) {
            when {
                c == '"' || c == '\'' -> {
                    isInQuote = !isInQuote
                }
                isInQuote -> {
                    sb.append(c)
                    continue
                }
                c == ' ' -> {
                    if (sb.isNotEmpty()) {
                        tokens.add(sb.toString())
                        sb.clear()
                    }
                }
                else -> {
                    sb.append(c)
                }
            }
        }

        if (sb.isNotEmpty()) {
            tokens.add(sb.toString())
        }

        val left = tokens[0]
        val operator = when (tokens[1]) {
            "==" -> CompareType.EQUALS
            "!=" -> CompareType.NOT_EQUALS
            "contains" -> CompareType.CONTAINS
            "startsWith" -> CompareType.STARTS_WITH
            "endsWith" -> CompareType.ENDS_WITH
            "length" -> CompareType.PROPERTY_ACCESS
            "uppercase" -> CompareType.PROPERTY_ACCESS
            "lowercase" -> CompareType.PROPERTY_ACCESS
            ">" -> CompareType.GREATER_THAN
            "<" -> CompareType.LESS_THAN
            else -> throw Exception("Unsupported operator ${tokens[1]}")
        }
        val right = tokens[2]
        // if right is String, remove double quote
        if (right.startsWith("\"") && right.endsWith("\"")) {
            return CompareExpr(left, operator, right.substring(1, right.length - 1))
        }

        return CompareExpr(left, operator, right)
    }

    private fun parsePropertyAccess(expression: String): PropertyAccess {
        val chars = expression.toCharArray()
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        for (c in chars) {
            when (c) {
                '.' -> {
                    if (sb.isNotEmpty()) {
                        tokens.add(sb.toString())
                        sb.clear()
                    }
                }
                else -> {
                    sb.append(c)
                }
            }
        }

        if (sb.isNotEmpty()) {
            tokens.add(sb.toString())
        }

        val propertyName = tokens[0]
        val operator = when (tokens[1]) {
            "length" -> AccessType.LENGTH
            "uppercase" -> AccessType.UPPER_CASE
            "lowercase" -> AccessType.LOWER_CASE
            else -> throw Exception("Unsupported operator ${tokens[1]}")
        }
        val value = tokens[2]
        return PropertyAccess(propertyName, operator, value)
    }
}

data class CompareExpr(val left: String, val operator: CompareType, val right: String)

enum class CompareType {
    EQUALS,
    NOT_EQUALS,
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    GREATER_THAN,
    LESS_THAN,
    PROPERTY_ACCESS,
}

data class PropertyAccess(val propertyName: String, val operator: AccessType, val value: String)

enum class AccessType {
    LENGTH,
    UPPER_CASE,
    LOWER_CASE,
}
