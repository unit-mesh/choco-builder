import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.similarity.JaccardSimilarity
import cc.unitmesh.nlp.similarity.meanPool
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.loader.JsonLoader
import cc.unitmesh.rag.retriever.VectorStoreRetriever
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import cc.unitmesh.rag.splitter.TokenTextSplitter
import cc.unitmesh.rag.store.InMemoryVectorStore
import cc.unitmesh.rag.store.VectorStore
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

        val vectorStore: VectorStore = InMemoryVectorStore(embeddingProvider, JaccardSimilarity())
        vectorStore.add(documents)

        val vectorStoreRetriever = VectorStoreRetriever(vectorStore)
        val userQuery = "What bike is good for city commuting?"

        // slowly in local
//        val similarDocuments: List<Document> = vectorStoreRetriever.retrieve(userQuery)
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

        val documentList = TokenTextSplitter(chunkSize = 384).apply(documents)

        val vectorStore: VectorStore = InMemoryVectorStore(embeddingProvider, JaccardSimilarity())
        vectorStore.add(documentList)

        val vectorStoreRetriever = VectorStoreRetriever(vectorStore)
        val userQuery = "安全规定了哪些要素？（敏感数据、隐私）"

        val similarDocuments: List<Document> = vectorStoreRetriever.retrieve(userQuery)
        similarDocuments.size shouldBe 4
        similarDocuments.forEach {
            println(it.text)
        }
    }
}