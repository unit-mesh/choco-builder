package cc.unitmesh.nlp.similarity

import cc.unitmesh.nlp.embedding.Embedding

interface Similarity {
    fun similarityScore(set1: Embedding, set2: Embedding) : Double
}

