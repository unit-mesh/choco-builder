package cc.unitmesh.rag.document

public data class TextSegment(
    val text: String,
    val metadata: Metadata,
) {
    companion object {
        fun from(text: String):TextSegment {
            return TextSegment(text, mapOf())
        }
    }
}