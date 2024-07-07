package cc.unitmesh.nlp.tokenizer

import cc.unitmesh.nlp.tokenizer.regexp.RegexTokenizerOptions
import cc.unitmesh.nlp.tokenizer.regexp.RegexpTokenizer

class WordTokenizer(options: RegexTokenizerOptions? = null) : RegexpTokenizer(options) {
    init {
        whitespacePattern = Regex("[^A-Za-zА-Яа-я0-9_]+")
    }
}