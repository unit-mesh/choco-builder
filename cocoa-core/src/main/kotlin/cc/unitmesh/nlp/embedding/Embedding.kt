package cc.unitmesh.nlp.embedding

// I don't know why, but sometimes the following import is not added automatically.
public typealias Embedding = List<Double>

fun toEmbedding(text: Iterable<Float>): Embedding {
    return text.map { it.toDouble() }
}

fun toEmbedding(text: FloatArray): Embedding {
    return text.map { it.toDouble() }
}