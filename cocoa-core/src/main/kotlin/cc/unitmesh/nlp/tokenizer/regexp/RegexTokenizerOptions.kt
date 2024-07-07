package cc.unitmesh.nlp.tokenizer.regexp

interface RegexTokenizerOptions {
    val pattern: Regex?
    val discardEmpty: Boolean
    val gaps: Boolean?
}