package cc.unitmesh.processor.api.base

interface ApiProcessor {
    fun convertApi(): List<ApiCollection>
}
