package cc.unitmesh.rag.store;

import cc.unitmesh.nlp.embedding.text.TextEmbeddingProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InMemoryTextStoreTest {
    private val provider = TextEmbeddingProvider()
    @Test
    fun should_find_relevant_embeddings() {
        // given
        val store = InMemoryTextStore<String>()

        store.add(provider.embed("this is a example"), "this is a example")
        store.add(provider.embed("this is a dog"), "this is a dog")
        store.add(provider.embed("this is item list"), "this is item list")

        val maxResults = 1
        val minScore = 0.5

        val embedding4 = provider.embed("this is a cat")

        // when
        val relevantEmbeddings = store.findRelevant(embedding4, maxResults, minScore)

        println(relevantEmbeddings)
        // then
        assertNotNull(relevantEmbeddings)
        assertEquals(1, relevantEmbeddings.size)
        assertEquals("this is a dog", relevantEmbeddings[0].embedded)
    }
}