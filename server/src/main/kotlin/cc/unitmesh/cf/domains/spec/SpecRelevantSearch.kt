package cc.unitmesh.cf.domains.spec

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.retriever.EmbeddingStoreRetriever
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import cc.unitmesh.rag.splitter.TokenTextSplitter
import cc.unitmesh.rag.store.EmbeddingStore
import cc.unitmesh.rag.store.InMemoryEmbeddingStore
import org.springframework.stereotype.Component

@Component
class SpecRelevantSearch(val embeddingProvider: EmbeddingProvider) {
    private lateinit var vectorStoreRetriever: EmbeddingStoreRetriever

    // cached for performance
    private val searchCache: MutableMap<String, List<SearchResult>> = mutableMapOf()

    init {
        val text = javaClass.getResourceAsStream("/be/specification.md")!!.bufferedReader().readText()
        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "H1"),
            Pair("##", "H2"),
        )

        val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
            .splitText(text)

        val documentList = documents.map {
            val header = "${it.metadata["H1"]} > ${it.metadata["H2"]}"
            val withHeader = it.copy(text = "$header ${it.text}")
            TokenTextSplitter(chunkSize = 384).apply(listOf(withHeader)).first()
        }

        val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()
        val embeddings: List<Embedding> = documentList.map {
            embeddingProvider.embed(it.text)
        }
        vectorStore.addAll(embeddings, documentList)

        this.vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore, 5, 0.6)
    }

    // TODO: change to search engine
    fun search(query: String): List<SearchResult> {
        if (searchCache.containsKey(query)) {
            return searchCache[query]!!
        }

        val queryEmbedding = embeddingProvider.embed(query)
        val similarDocuments = vectorStoreRetriever.retrieve(queryEmbedding)
        val results = similarDocuments.map {
                SearchResult(
                    source = it.embedded.metadata.toString(),
                    content = it.embedded.text
                )
        }
        searchCache[query] = results
        return results
    }
}

data class SearchResult(
    val source: String,
    val content: String
)