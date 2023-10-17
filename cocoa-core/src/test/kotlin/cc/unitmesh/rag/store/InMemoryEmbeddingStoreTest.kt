package cc.unitmesh.rag.store;

import cc.unitmesh.docs.SampleCode
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.toEmbedding
import cc.unitmesh.rag.document.Document
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class InMemoryEmbeddingStoreTest {
    @Test
    @SampleCode
    fun it_works() {
        // start-sample
        val embeddingStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()

        embeddingStore.add(toEmbedding(floatArrayOf(1f, 3f)), Document.from("first"))
        embeddingStore.add(toEmbedding(floatArrayOf(2f, 2f)), Document.from("second"))

        val relevant: List<EmbeddingMatch<Document>> =
            embeddingStore.findRelevant(toEmbedding(floatArrayOf(4f, 0f)), 2)

        // end-sample

    }

    @Test
    @SampleCode(name = "文本嵌入示例", content = "")
    fun should_add_embedding_with_generated_id() {
        val embeddingStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()

        val embedding1: Embedding = toEmbedding(floatArrayOf(1f, 3f))
        val segment1 = Document.from("first")
        embeddingStore.add(embedding1, segment1)

        val embedding2: Embedding = toEmbedding(floatArrayOf(2f, 2f))
        val segment2 = Document.from("second")
        val id2: String = embeddingStore.add(embedding2, segment2)

        val embedding3: Embedding = toEmbedding(floatArrayOf(3f, 1f))
        val segment3 = Document.from("third")
        val id3: String = embeddingStore.add(embedding3, segment3)

        val relevant: List<EmbeddingMatch<Document>> =
            embeddingStore.findRelevant(toEmbedding(floatArrayOf(4f, 0f)), 2)

        Assertions.assertThat(relevant).containsExactly(
            EmbeddingMatch(0.9743416490252569, id3, embedding3, segment3),
            EmbeddingMatch(0.8535533905932737, id2, embedding2, segment2)
        )
    }

}