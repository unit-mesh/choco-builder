package cc.unitmesh.connection

import io.vertx.core.json.JsonObject
import io.vertx.json.schema.JsonSchema
import io.vertx.json.schema.JsonSchemaOptions
import io.vertx.json.schema.SchemaRepository
import java.io.File


class ConnectionParser {
    fun loadSchemaByType() {
        val context = File(loadResource()).readText()
        val obj = JsonObject(context)
        val schema: JsonSchema = JsonSchema.of(obj)

        schema
    }


    private fun loadResource(): String {
        return this.javaClass.getResource("/schemas/AzureOpenAIConnection.schema.json").file
    }
}