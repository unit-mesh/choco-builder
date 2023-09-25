package cc.unitmesh.prompt

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File


class PromptManager {
    fun loadFile(fileName: String): JsonObject? {
        return try {
            val fileContent = File(fileName).readText()
            val jsonObject = JsonParser.parseString(fileContent)?.getAsJsonObject()
            jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun engine() {

    }
}