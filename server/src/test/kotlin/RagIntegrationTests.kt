import cc.unitmesh.cf.LocalEmbedding
import cc.unitmesh.cf.STEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.SentenceTransformersEmbedding
import cc.unitmesh.nlp.embedding.Embedding
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
    val semantic = LocalEmbedding.create()

    private val embeddingProvider = SentenceTransformersEmbedding()

    @Test
    fun should_able_to_search_by_documents_by_json() {
        val inputStream = javaClass.getResourceAsStream("/rag/bikes.json")!!

        val jsonLoader = JsonLoader(inputStream, listOf("name", "price", "shortDescription", "description"))
        val documents = jsonLoader.load(TokenTextSplitter(encoding = embeddingProvider, chunkSize = 500))

        val documentList = TokenTextSplitter(encoding = embeddingProvider, chunkSize = 500).apply(documents)
        val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()
        val embeddings: List<Embedding> = documentList.map {
            embeddingProvider.embed(it.text)
        }

        vectorStore.addAll(embeddings, documentList)

        val vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore)
        val userQuery = embeddingProvider.embed("What bike is good for city commuting?")

        // slowly in local
        val similarDocuments: List<EmbeddingMatch<Document>> = vectorStoreRetriever.retrieve(userQuery)
        similarDocuments.size shouldBe 4

        similarDocuments.forEach {
            println(it.embedded.text)
        }
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
        val embeddingProvider = SentenceTransformersEmbedding()
        val text = javaClass.getResourceAsStream("/rag/be.md")!!.bufferedReader().readText();

        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "H1"),
            Pair("##", "H2"),
//            Pair("###", "H3"),
        )

        val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
            .splitText(text)


        // 去尾处理，如果文本长度大于 384，就截断。只取前 384 个字符
        val documentList = documents.map {
            val header = "${it.metadata["H1"]} > ${it.metadata["H2"]}"
//            if (it.metadata.containsKey("H3")) {
//                header += "$header > ${it.metadata["H3"]}"
//            }

            val withHeader = it.copy(text = "$header ${it.text}")
            TokenTextSplitter(chunkSize = 384).apply(listOf(withHeader)).first()
        }

        val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()
        val embeddings: List<Embedding> = documentList.map {
            embeddingProvider.embed(it.text)
        }
        vectorStore.addAll(embeddings, documentList)

        val vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore, 5, 0.6)

        vectorStoreRetriever.retrieve(embeddingProvider.embed("如何处理敏感数据"))
            .forEach {
                println(it.score.toString() + it.embedded.metadata.toString())
                println(it.embedded.text)
            }
    }
}