package cc.unitmesh.connection;

import org.junit.jupiter.api.Test

class SchemaLoaderTest {

    @Test
    fun shouldLoadSchemaByType() {
        // given
        val schemaLoader = ConnectionParser()

        // when
        val result = schemaLoader.loadSchemaByType()

        // then
        // Add assertions here to verify the result
    }
}