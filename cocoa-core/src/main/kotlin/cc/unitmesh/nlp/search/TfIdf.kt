/*
Copyright (c) 2011, Rob Ellis, Chris Umbel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package cc.unitmesh.nlp.search

import cc.unitmesh.nlp.tokenizer.regexp.RegexpTokenizer
import cc.unitmesh.nlp.tokenizer.Tokenizer
import cc.unitmesh.nlp.tokenizer.WordTokenizer
import java.lang.IllegalArgumentException
import kotlin.collections.count
import kotlin.collections.filterIsInstance
import kotlin.collections.fold
import kotlin.collections.forEach
import kotlin.collections.get
import kotlin.collections.indices
import kotlin.collections.set
import kotlin.collections.sortedByDescending
import kotlin.math.ln
import kotlin.to

var ourStopwords = listOf(
    "about", "above", "after", "again", "all", "also", "am", "an", "and", "another",
    "any", "are", "as", "at", "be", "because", "been", "before", "being", "below",
    "between", "both", "but", "by", "came", "can", "cannot", "come", "could", "did",
    "do", "does", "doing", "during", "each", "few", "for", "from", "further", "get",
    "got", "has", "had", "he", "have", "her", "here", "him", "himself", "his", "how",
    "if", "in", "into", "is", "it", "its", "itself", "like", "make", "many", "me",
    "might", "more", "most", "much", "must", "my", "myself", "never", "now", "of", "on",
    "only", "or", "other", "our", "ours", "ourselves", "out", "over", "own",
    "said", "same", "see", "she", "should", "since", "so", "some", "still", "such", "take", "than",
    "that", "the", "their", "theirs", "them", "themselves", "then", "there", "these", "they",
    "this", "those", "through", "to", "too", "under", "until", "up", "very", "was",
    "way", "we", "well", "were", "what", "where", "when", "which", "while", "who",
    "whom", "with", "would", "why", "you", "your", "yours", "yourself",
    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "$", "1",
    "2", "3", "4", "5", "6", "7", "8", "9", "0", "_"
)

typealias DocumentType = Any
typealias TfIdfCallback = (index: Int, measure: Double, key: Any?) -> Unit

class TfIdf<K, V> {
    private val documents: MutableList<DocumentType> = mutableListOf()
    private var _idfCache: MutableMap<String, Double> = mutableMapOf()

    var tokenizer: RegexpTokenizer = WordTokenizer()

    companion object {
        fun tf(term: String, document: DocumentType): Int {
            return (document as? Map<*, *>)?.get(term) as? Int ?: 0
        }
    }

    fun idf(term: String, force: Boolean = false): Double {
        if (_idfCache[term] != null && !force) {
            return _idfCache[term]!!
        }

        val docsWithTerm = documents.count { documentHasTerm(term, it) }
        val idf = 1 + ln((documents.size.toDouble()) / (1 + docsWithTerm))
        _idfCache[term] = idf
        return idf
    }

    private fun documentHasTerm(term: String, document: DocumentType): Boolean {
        return ((document as? Map<*, *>)?.get(term) as? Int ?: 0) > 0
    }

    fun buildDocument(text: DocumentType, key: Any? = null): MutableMap<String, Any?> {
        val stopOut: Boolean
        val doc: MutableMap<String, Any?>

        when (text) {
            is String -> {
                val tokens = tokenizer.tokenize(text)
                stopOut = true
                doc = tokens.fold(mutableMapOf("__key" to key)) { acc, term ->
                    if (term !in ourStopwords) {
                        acc[term] = (acc[term] as? Int ?: 0) + 1
                    }
                    acc
                }
            }

            is List<*> -> {
                stopOut = false
                doc = text.filterIsInstance<String>()
                    .fold(mutableMapOf("__key" to key)) { acc, term ->
                        acc[term] = (acc[term] as? Int ?: 0) + 1
                        acc
                    }
            }

            else -> {
                stopOut = false
                doc = (text as MutableMap<String, Any?>)
            }
        }


        // remove "__key"
        if (doc.containsKey("__key")) {
            doc.remove("__key")
        }

        return doc
    }

    fun addDocument(document: DocumentType, key: Any? = null, restoreCache: Boolean = false) {
        documents.add(buildDocument(document, key))

        if (restoreCache) {
            for (term in _idfCache.keys) {
                idf(term, true)
            }
        } else {
            _idfCache = mutableMapOf()
        }
    }

    fun addDocuments(documents: List<DocumentType>) {
        documents.forEach { addDocument(it) }
    }

    fun tfidf(terms: Any, d: Int): Double {
        val termsList = if (terms is String) {
            tokenizer.tokenize(terms)
        } else {
            terms as List<String>
        }

        return termsList.fold(0.0) { value, term ->
            val idf = idf(term)
            value + (tf(term, documents[d]) * if (idf == Double.POSITIVE_INFINITY) 0.0 else idf)
        }
    }

    fun listTerms(index: Int): List<TermData> {
        val terms = mutableListOf<TermData>()
        for ((term, value) in documents[index] as Map<String, Int>) {
            if (term != "__key") {
                terms.add(TermData(term, tf(term, documents[index]), idf(term), tfidf(term, index)))
            }
        }

        return terms.sortedByDescending { it.tfidf }
    }

    data class TermData(val term: String, val tf: Int, val idf: Double, val tfidf: Double)

    /**
     * This function calculates the Term Frequency-Inverse Document Frequency (TF-IDF) for each document in the collection.
     * TF-IDF is a numerical statistic that reflects how important a word is to a document in a collection or corpus.
     *
     * @param terms The terms for which the TF-IDF is to be calculated. This can be a single term or a collection of terms.
     * @param callback An optional callback function that is invoked for each document. The callback function is passed three parameters:
     *                 - The index of the current document.
     *                 - The calculated TF-IDF value for the current document.
     *                 - The key of the current document (if it exists).
     *                 The callback function can be used to perform additional processing or logging for each document.
     *
     * @return A list of Double values representing the calculated TF-IDF values for each document in the collection. The order of the values in the list corresponds to the order of the documents in the collection.
     */
    fun tfidfs(terms: Any, callback: TfIdfCallback? = null): List<Double> {
        val tfidfs = MutableList(documents.size) { 0.0 }

        for (i in documents.indices) {
            tfidfs[i] = tfidf(terms, i)
            callback?.invoke(i, tfidfs[i], (documents[i] as? Map<String, Any>)?.get("__key"))
        }

        return tfidfs
    }

    fun setTokenizer(t: Any) {
        if (t !is Tokenizer) {
            throw IllegalArgumentException("Expected a valid Tokenizer")
        }

        tokenizer = t as RegexpTokenizer
    }

    fun setStopwords(customStopwords: List<String>): Boolean {
        ourStopwords = customStopwords
        return true
    }

    fun search(query: String): List<Double> {
        return tfidfs(query, null)
    }
}
