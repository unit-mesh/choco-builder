import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.similarity.JaccardSimilarity
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.loader.JsonLoader
import cc.unitmesh.rag.retriever.VectorStoreRetriever
import cc.unitmesh.rag.splitter.TokenTextSplitter
import cc.unitmesh.rag.store.InMemoryVectorStore
import cc.unitmesh.rag.store.VectorStore
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RagIntegrationTests {
    val semantic = STSemantic.create()

    @Test
    fun should_able_to_search_by_documents() {
        val inputStream = javaClass.getResourceAsStream("/rag/bikes.json")!!

        val jsonLoader = JsonLoader(inputStream, listOf("name", "price", "shortDescription"))
        val documents = jsonLoader.load(TokenTextSplitter(
            chunkSize = 400,
        ))

        val embeddingProvider = object : EmbeddingProvider {
            override fun embed(texts: List<String>): List<Embedding> {
                return texts.map {
                    semantic.embed(it).toList()
                }
            }
        }

        val vectorStore: VectorStore = InMemoryVectorStore(embeddingProvider, JaccardSimilarity())
        vectorStore.add(documents)

        val vectorStoreRetriever = VectorStoreRetriever(vectorStore)
        val userQuery = "What bike is good for city commuting?"

//        val similarDocuments: List<Document> = vectorStoreRetriever.retrieve(userQuery)
//        similarDocuments.size shouldBe 4
    }
}