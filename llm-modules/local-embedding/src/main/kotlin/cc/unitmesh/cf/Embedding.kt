package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer

interface Embedding {
    fun getTokenizer(): HuggingFaceTokenizer
    fun embed(input: String): List<Double>
}