package cc.unitmesh.nlp.embedding

interface EncodingTokenizer {
    fun encode(text: String): List<Int>
    fun decode(tokens: List<Int>): String
}