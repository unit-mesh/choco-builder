package cc.unitmesh.processor.api.parser

import cc.unitmesh.processor.api.base.ApiCollection
import cc.unitmesh.processor.api.base.ApiProcessor
import cc.unitmesh.processor.api.postman.PostmanReader
import java.io.File

class PostmanProcessor(val file: File) : ApiProcessor {
    override fun convertApi(): List<ApiCollection> {
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(file.absolutePath)
        val postmanParser = PostmanParser()

        return postmanParser.parse(collection) ?: emptyList()
    }
}
