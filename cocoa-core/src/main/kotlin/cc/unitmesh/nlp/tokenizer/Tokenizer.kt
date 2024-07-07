package cc.unitmesh.nlp.tokenizer

import kotlin.collections.first
import kotlin.collections.last
import kotlin.collections.lastIndex

interface Tokenizer {
    fun tokenize(text: String): List<String>
    open fun trim(array: MutableList<String>): List<String> {
        while (array.last() == "") {
            array.removeAt(array.lastIndex)
        }

        while (array.first() == "") {
            array.removeAt(0)
        }

        return array
    }
}