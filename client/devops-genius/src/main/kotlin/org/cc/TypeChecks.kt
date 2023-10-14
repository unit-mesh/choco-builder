package org.cc

/**
 * Determines whether a given character is a whitespace character.
 * <whitespace> ::= <ZWNBSP> | <TAB> | <VT> | <FF> | <SP> | <NBSP> | <USP>
 */
fun isWhitespace(content: String): Boolean {
    val token = content[0]
    return token == ZWNBSP || token == TAB || token == VT || token == FF || token == SP || token == NBSP
}

/**
 * Determines whether a given character is a newline character.
 * <newline> ::= <CR>? <LF>
 */
fun isNewline(token: String): Boolean {
    if (token.isEmpty()) {
        return false
    }

    val char = token[0]
    return char == CR || char == LF
}

/**
 * Determines whether a given character is a parenthesis.
 * <parens> ::= "(" | ")"
 */
fun isParens(token: String): Boolean {
    val char = token[0]
    return char == '(' || char == ')'
}
