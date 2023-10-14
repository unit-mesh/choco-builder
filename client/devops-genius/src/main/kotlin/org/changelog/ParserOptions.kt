package org.changelog

data class ParserOptions(
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
)                   {
    companion object {
        fun defaultOptions(): ParserOptions {
            return ParserOptions(
                noteKeywords = listOf("BREAKING CHANGE", "BREAKING-CHANGE"),
                issuePrefixes = listOf("#"),
                referenceActions = listOf(
                    "close",
                    "closes",
                    "closed",
                    "fix",
                    "fixes",
                    "fixed",
                    "resolve",
                    "resolves",
                    "resolved"
                ),
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\-*/ ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf(
                    "type",
                    "scope",
                    "subject"
                ),
                revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (\\w*)\\."),
                revertCorrespondence = listOf("header", "hash"),
                fieldPattern = Regex("^-(.*?)-$")
            )
        }
    }
}

//
///**
// * What warn function to use. For example, `console.warn.bind(console)`. By default, it's a noop. If it is `true`, it will error if commit cannot be parsed (strict).
// */
//class ParserStreamOptions(
//    warn: Boolean? = null,
//    warnFunction: ((String) -> Unit)? = null,
//) : ParserOptions(
//
//)