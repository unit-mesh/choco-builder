package cc.unitmesh.cf.infrastructure.llms.embedding

import org.springframework.stereotype.Component
import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.embedding.EncodingTokenizer

@Component
class SentenceTransformersEmbedding : EmbeddingProvider, EncodingTokenizer {
    val semantic = STSemantic.create()
    val tokenizer = semantic.getTokenizer()

    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }

    override fun encode(text: String): List<Int> {
        val encode = tokenizer.encode(text)
        val result = encode.ids.map { it.toInt() }
        // remove the first and last token
        return result.subList(1, result.size - 1)
    }

    override fun decode(tokens: List<Int>): String {
        val map = tokens.map { it.toLong() }.toLongArray()
        // output will be "[CLS] blog [SEP]" for input "blog", so we need to remove the first and last token
        return tokenizer.decode(map)
    }
}