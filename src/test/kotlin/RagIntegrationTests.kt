import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RagIntegrationTests {
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
}