package org.changelog

open class ParserOptions(
    val commentChar: String? = null,
    val mergePattern: Regex? = null,
    val mergeCorrespondence: List<String>? = null,
    val headerPattern: Regex? = null,
    val breakingHeaderPattern: Regex? = null,
    val headerCorrespondence: List<String>? = null,
    val revertPattern: Regex? = null,
    val revertCorrespondence: List<String>? = null,
    val fieldPattern: Regex? = null,
    val noteKeywords: List<String>? = null,
    val notesPattern: ((String) -> Regex)? = null,
    val issuePrefixes: List<String>? = null,
    val issuePrefixesCaseSensitive: Boolean? = null,
    val referenceActions: List<String>? = null,
)


/**
 * What warn function to use. For example, `console.warn.bind(console)`. By default, it's a noop. If it is `true`, it will error if commit cannot be parsed (strict).
 */
class ParserStreamOptions(
    warn: Boolean? = null,
    warnFunction: ((String) -> Unit)? = null,
) : ParserOptions(

)