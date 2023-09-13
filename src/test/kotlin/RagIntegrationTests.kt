import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.loader.JsonLoader
import cc.unitmesh.rag.retriever.VectorStoreRetriever
import cc.unitmesh.rag.store.InMemoryVectorStore
import cc.unitmesh.rag.store.VectorStore
import org.junit.jupiter.api.Test

class RagIntegrationTests {
    val semantic = STSemantic.create()

    @Test
    fun should_able_to_search_by_documents() {
        val inputStream = javaClass.getResourceAsStream("/rag/bikes.json")!!

        val jsonLoader = JsonLoader(inputStream, listOf("name", "price", "shortDescription", "description"))
        val documents = jsonLoader.load()

        val embeddingProvider = object : EmbeddingProvider {
            override fun embed(texts: List<String>): List<Embedding> {
                return texts.map {
                    semantic.embed(it).toList()
                }
            }

        }
        val vectorStore: VectorStore = InMemoryVectorStore(embeddingProvider)

        vectorStore.add(documents)

        val vectorStoreRetriever = VectorStoreRetriever(vectorStore)
        val userQuery = "What bike is good for city commuting?"

        val similarDocuments: List<Document> = vectorStoreRetriever.retrieve(userQuery)
        println(similarDocuments)
    }
}