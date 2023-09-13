package cc.unitmesh.cf.core.nlp.similarity

import cc.unitmesh.cf.core.llms.Embedding

interface Similarity {
    fun similarityScore(set1: Embedding, set2: Embedding) : Double
}


class SimilarityScore(val key: String, val similarity: Double)
