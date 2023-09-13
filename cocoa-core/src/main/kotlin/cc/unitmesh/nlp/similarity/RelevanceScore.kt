package cc.unitmesh.nlp.similarity


object RelevanceScore {
    fun fromCosineSimilarity(cosineSimilarity: Double): Double {
        return (cosineSimilarity + 1) / 2
    }
}
