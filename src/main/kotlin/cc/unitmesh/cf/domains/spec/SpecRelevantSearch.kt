package cc.unitmesh.cf.domains.spec

import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.similarity.JaccardSimilarity
import cc.unitmesh.rag.retriever.VectorStoreRetriever
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import cc.unitmesh.rag.splitter.TokenTextSplitter
import cc.unitmesh.rag.store.InMemoryVectorStore
import cc.unitmesh.rag.store.VectorStore
import org.springframework.stereotype.Component

@Component
class SpecRelevantSearch(val embeddingProvider: EmbeddingProvider) {
    private lateinit var vectorStoreRetriever: VectorStoreRetriever

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

        val vectorStore: VectorStore = InMemoryVectorStore(embeddingProvider, JaccardSimilarity())
        vectorStore.add(documentList)

        this.vectorStoreRetriever = VectorStoreRetriever(vectorStore)
    }

    // TODO: change to search engine
    fun search(query: String): List<String> {
        if (searchCache.containsKey(query)) {
            return searchCache[query]!!
        }

        val similarDocuments = vectorStoreRetriever.retrieve(query)
        val results = similarDocuments.map { it.text }
        searchCache[query] = results
        return results
    }
}