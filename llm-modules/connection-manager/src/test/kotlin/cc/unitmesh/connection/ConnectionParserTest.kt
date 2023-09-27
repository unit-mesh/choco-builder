package cc.unitmesh.connection;

import org.junit.jupiter.api.Test

class ConnectionParserTest {

    @Test
    fun shouldLoadSchemaByType() {
        // given
        val schemaLoader = ConnectionParser()

        // when
        val result = schemaLoader.loadSchemaByType()

        println(result)
        // then
        // Add assertions here to verify the result
    }
}