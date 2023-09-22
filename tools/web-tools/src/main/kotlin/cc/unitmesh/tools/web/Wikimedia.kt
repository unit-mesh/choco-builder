package cc.unitmesh.tools.web

import cc.unitmesh.agent.Tool
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Tool(name = "wikimedia", value = ["wikimedia"])
class Wikimedia {
    val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/117.0.0.0 Safari/537.36 Edg/113.0.1774.35"
    )

    private fun removeNestedParentheses(string: String): String {
        val pattern = "\\([^()]+\\)".toRegex()
        var result = string
        while (pattern.containsMatchIn(result)) {
            result = pattern.replace(result, "")
        }
        return result
    }

    val urlList = mutableListOf<String>()
    fun run(title: String): MutableList<String> {
        val url = url(title)
        val doc = Jsoup.connect(url).headers(headers).get()
        val mwDivs = doc.select("div.mw-search-result-heading")
        if (mwDivs.isNotEmpty()) {
            val resultTitles = mwDivs.map { it.text().trim() }
                .map { removeNestedParentheses(it) }

            urlList.addAll(resultTitles.map { url(it) })
        } else {
            val pageContent = (doc.select("p") + doc.select("ul")).map { it.text().trim() }
            if (pageContent.any { "may refer to:" in it }) {
                urlList.addAll(run("[$title]"))
            } else {
                urlList.add(url)
            }
        }

        return urlList
    }

    private fun url(it: String) =
        "https://en.wikipedia.org/w/index.php?search=${URLEncoder.encode(it, StandardCharsets.UTF_8)}"
}
