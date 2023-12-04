package cc.unitmesh.processor.api.render

import cc.unitmesh.processor.api.base.ApiCollection
import cc.unitmesh.processor.api.base.ApiDetailRender
import cc.unitmesh.processor.api.base.ApiTagOutput

class MarkdownTableRender : ApiDetailRender {
    override fun renderCollection(collection: ApiCollection): String {
        val result: MutableList<String> = mutableListOf()

        // if a name starts with number and ".", return ""
        // for example, "3. Transfer state simulation" will return ""
        val name = collection.name
        if (name.matches(Regex("^\\d+\\..*"))) {
            return ""
        }

        if (collection.items.isEmpty()) {
            return ""
        }

        if (collection.name.isNotEmpty()) {
            result += listOf("## ${collection.name}\n")
        }

        if (collection.description.isNotEmpty()) {
            result += listOf("> ${collection.description}")
        }

        result += listOf("| API | Method | Description | Request | Response | Error Response |")
        result += listOf("| --- | --- | --- | --- | --- | --- |")
        collection.items.forEach { detail ->
            val api = detail.path
            val method = detail.method
            val description = detail.description
            val request = detail.request.toString()
            val response = detail.response.toString()
            val errorResponse = "400: {\"error\": String}"
            result += listOf("| $api | $method | $description | $request | $response | $errorResponse |")
        }

        return ApiTagOutput(result.joinToString("\n")).toString()
    }
}
