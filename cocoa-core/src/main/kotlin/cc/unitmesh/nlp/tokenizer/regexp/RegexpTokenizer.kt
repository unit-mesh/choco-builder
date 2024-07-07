package cc.unitmesh.nlp.tokenizer.regexp

import cc.unitmesh.nlp.tokenizer.Tokenizer
import kotlin.collections.contains
import kotlin.collections.filter
import kotlin.collections.ifEmpty
import kotlin.sequences.map
import kotlin.sequences.toList
import kotlin.text.split

open class RegexpTokenizer(opts: RegexTokenizerOptions? = null) : Tokenizer {
    var whitespacePattern = Regex("\\s+")
    var discardEmpty: Boolean = true
    private var _gaps: Boolean? = null

    init {
        val options = opts ?: object : RegexTokenizerOptions {
            override val pattern: Regex? = null
            override val discardEmpty: Boolean = true
            override val gaps: Boolean? = null
        }

        whitespacePattern = options.pattern ?: whitespacePattern
        discardEmpty = options.discardEmpty
        _gaps = options.gaps

        if (_gaps == null) {
            _gaps = true
        }
    }

    override fun tokenize(text: String): List<String> {
        val results: List<String>

        if (_gaps == true) {
            results = text.split(whitespacePattern)
            return if (discardEmpty) without(results, "", " ") else results
        } else {
            results = whitespacePattern.findAll(text).map { it.value }.toList()
            return results.ifEmpty { emptyList() }
        }
    }

    fun without(arr: List<String>, vararg values: String): List<String> {
        return arr.filter { it !in values }
    }
}