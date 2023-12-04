package cc.unitmesh.processor.api.parser

import cc.unitmesh.processor.api.postman.PostmanReader
import cc.unitmesh.processor.api.render.MarkdownTableRender
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class PostmanParserTest {
    @Test
    fun should_print_out() {
        val file = javaClass.getResource("/openapi/CircleCI.postman_collection.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(file.toURI()).absolutePath)
        val postmanParser = PostmanParser()
        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 17
        listList[0].items.size shouldBe 2

        val output = MarkdownTableRender().render(listList)
        println(output)
    }

    @Test
    fun should_print_out_2() {
        val boxJson = javaClass.getResource("/openapi/Box.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(boxJson.toURI()).absolutePath)
        val postmanParser = PostmanParser()

        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 1
        listList[0].items.size shouldBe 5

        val output = MarkdownTableRender().render(listList)
        println(output)
    }

    @Test
    fun should_hande_description() {
        val boxJson = javaClass.getResource("/postman/Wechat-Simple.postman_collection.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(boxJson.toURI()).absolutePath)
        val postmanParser = PostmanParser()

        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 1
        listList[0].items.size shouldBe 10

        val output = MarkdownTableRender().render(listList)
        println(output)
    }
}
