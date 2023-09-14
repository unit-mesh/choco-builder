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
    private val searchCache: MutableMap<String, List<String>> = mutableMapOf()

    init {
        val text = javaClass.getResourceAsStream("/be/specification.md")!!.bufferedReader().readText()
        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2"),
            Pair("###", "Header 3"),
        )

        val documents = MarkdownHeaderTextSplitter(headersToSplitOn)
            .splitText(text)

        val documentList = TokenTextSplitter(chunkSize = 384).apply(documents)

        val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()
        val embeddings: List<Embedding> = documentList.map {
            embeddingProvider.embed(it.text)
        }
        vectorStore.addAll(embeddings, documentList)

        this.vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore)
    }

    // TODO: change to search engine
    fun search(query: String): List<String> {
        if (searchCache.containsKey(query)) {
            return searchCache[query]!!
        }

        val queryEmbedding = embeddingProvider.embed(query)
        val similarDocuments = vectorStoreRetriever.retrieve(queryEmbedding)
        val results = similarDocuments.map { it.embedded.text }
        searchCache[query] = results
        return results
    }
}