package cc.unitmesh.processor.api

import cc.unitmesh.processor.api.postman.PostmanCollection
import cc.unitmesh.processor.api.postman.PostmanReader
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File

class ModelSerializeTest {
    val file = javaClass.getResource("/openapi/CircleCI.postman_collection.json")!!
    private val text = file.readText()

    @Test
    fun should_success_get_info_name_when_give_a_collection_file() {
        // deserialize text to PostmanCollection
        val collection = Json.decodeFromString(PostmanCollection.serializer(), text)
        collection.info!!.name shouldBe "CircleCI"
    }

    @Test
    fun should_get_info_by_reader() {
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(file.toURI()).absolutePath)

        collection.info!!.name shouldBe "CircleCI"
    }
}
