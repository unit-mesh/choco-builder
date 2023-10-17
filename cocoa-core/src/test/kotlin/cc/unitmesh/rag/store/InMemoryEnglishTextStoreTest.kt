package cc.unitmesh.rag.store;

import cc.unitmesh.docs.SampleCode
import cc.unitmesh.nlp.embedding.text.EnglishTextEmbeddingProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InMemoryEnglishTextStoreTest {
    private val provider = EnglishTextEmbeddingProvider()
    @Test
    @SampleCode
    fun should_find_relevant_embeddings() {
        // start-sample
        val store = InMemoryEnglishTextStore<String>()

        store.add(provider.embed("this is a example"), "this is a example")
        store.add(provider.embed("this is a dog"), "this is a dog")
        store.add(provider.embed("this is item list"), "this is item list")

        val maxResults = 1
        val minScore = 0.5

        val embedding4 = provider.embed("this is a cat")
        // end-sample

        // when
        val relevantEmbeddings = store.findRelevant(embedding4, maxResults, minScore)

        println(relevantEmbeddings)
        // then
        assertNotNull(relevantEmbeddings)
        assertEquals(1, relevantEmbeddings.size)
        assertEquals("this is a dog", relevantEmbeddings[0].embedded)
    }
}