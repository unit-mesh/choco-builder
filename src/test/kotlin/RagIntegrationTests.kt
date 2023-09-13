import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.loader.JsonLoader
import cc.unitmesh.rag.retriever.EmbeddingStoreRetriever
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import cc.unitmesh.rag.splitter.TokenTextSplitter
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import cc.unitmesh.rag.store.InMemoryEmbeddingStore
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RagIntegrationTests {
    val semantic = STSemantic.create()

    private val embeddingProvider = object : EmbeddingProvider {
        override fun embed(texts: List<String>): List<Embedding> {
            return texts.map {
                semantic.embed(it).toList()
            }
        }
    }

    @Test
    fun should_able_to_search_by_documents_by_json() {
        val inputStream = javaClass.getResourceAsStream("/rag/bikes.json")!!

        val jsonLoader = JsonLoader(inputStream, listOf("name", "price", "shortDescription"))
        val documents = jsonLoader.load(
            TokenTextSplitter(
                chunkSize = 400,
            )
        )

        val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()

        val embeddings: List<Embedding> = documents.map {
            embeddingProvider.embed(it.text)
        }

        vectorStore.addAll(embeddings, documents)

        val vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore)
        val userQuery = embeddingProvider.embed("What bike is good for city commuting?")

        // slowly in local
//        val similarDocuments: List<EmbeddingMatch<Document>> = vectorStoreRetriever.retrieve(userQuery)
//        similarDocuments.size shouldBe 4
    }

    @Test
    fun should_able_search_by_markdown() {
        val text = javaClass.getResourceAsStream("/rag/be.md")!!.bufferedReader().readText();

        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2"),
            Pair("###", "Header 3"),
        )

        val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
            .splitText(text)
            .map {
                if (it.text.length > 512) {
                    it.copy(text = it.text.substring(0, 512))
                } else {
                    it
                }
            }

        documents.size shouldBe 13
    }

    @Test
    fun should_able_search_for_sentence() {
        val text = javaClass.getResourceAsStream("/rag/be.md")!!.bufferedReader().readText();

        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2"),
            Pair("###", "Header 3"),
        )

        val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
            .splitText(text)

        documents.size shouldBe 13
    }
}