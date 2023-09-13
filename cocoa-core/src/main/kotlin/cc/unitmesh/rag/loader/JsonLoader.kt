package cc.unitmesh.rag.loader

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.splitter.TextSplitter
import cc.unitmesh.rag.splitter.TokenTextSplitter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream

// todo: use <DataFrame> in future
class JsonLoader(
    private val inputStream: InputStream,
    private var jsonKeysToUse: List<String> = listOf(),
) : Loader {
    override fun load(): List<Document> {
        return load(TokenTextSplitter())
    }

    override fun load(textSplitter: TextSplitter): List<Document> {
        val objectMapper = ObjectMapper()
        val documents: List<Document>
        try {
            // TODO, not all json will be an array
            val jsonData: List<Map<String, Any>> =
                objectMapper.readValue(inputStream, object : TypeReference<List<Map<String, Any>>>() {})

            documents = load(jsonData)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return textSplitter.apply(documents)
    }

    fun load(jsonData: List<Map<String, Any>>): List<Document> {
        val documents: MutableList<Document> = ArrayList()

        for (item in jsonData) {
            val sb = StringBuilder()
            for (key in jsonKeysToUse) {
                if (item.containsKey(key)) {
                    sb.append(key)
                    sb.append(": ")
                    sb.append(item[key])
                    sb.append(System.lineSeparator())
                }
            }

            if (sb.isNotEmpty()) {
                val document = Document(sb.toString())
                documents.add(document)
            } else {
                val document = Document(item.toString())
                documents.add(document)
            }
        }

        return documents
    }
}