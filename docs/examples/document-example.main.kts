@file:DependsOn("cc.unitmesh:rag-script:0.3.2")

import cc.unitmesh.rag.*

rag("code") {
    llm = LlmConnector(LlmType.OpenAI)
    embedding = EmbeddingEngine(EngineType.SentenceTransformers)
    store = Store(StoreType.Memory)

    indexing {
        document("filename.txt").split().also {
            store.indexing(it)
        }
    }

    querying {
        store.findRelevant("workflow dsl design ").also {
            println(it)
        }
    }
}