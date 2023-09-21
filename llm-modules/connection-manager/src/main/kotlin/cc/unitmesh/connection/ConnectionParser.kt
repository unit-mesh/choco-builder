package cc.unitmesh.connection

import io.vertx.core.json.JsonObject
import io.vertx.json.schema.JsonSchema
import io.vertx.json.schema.impl.JsonRef
import java.io.File


class ConnectionParser {
    fun loadSchemaByType(): JsonObject? {
        val context = File(loadResource()).readText()
        val obj = JsonObject(context)

        val resolved = JsonRef.resolve(obj)
        val ref = resolved.getJsonObject("definitions")

        return ref
    }


    private fun loadResource(): String {
        return this.javaClass.getResource("/schemas/AzureOpenAIConnection.schema.json").file
    }
}