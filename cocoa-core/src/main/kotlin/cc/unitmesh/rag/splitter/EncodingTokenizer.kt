package cc.unitmesh.rag.splitter

interface EncodingTokenizer {
    fun encode(text: String): List<Int>
    fun decode(tokens: List<Int>): String
}