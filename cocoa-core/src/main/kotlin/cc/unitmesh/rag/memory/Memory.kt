package cc.unitmesh.rag.memory

interface Memory {
    fun getKeys(): List<String>

    fun load(inputs: Map<String, Any>): Map<String, Any>

    fun save(inputs: Map<String, Any>, outputs: Map<String, Any>)
}