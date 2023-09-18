package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer

interface Semantic {
    fun getTokenizer(): HuggingFaceTokenizer
    fun embed(input: String): List<Double>
}