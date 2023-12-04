package cc.unitmesh.processor.api

import cc.unitmesh.processor.api.parser.PostmanProcessor
import cc.unitmesh.processor.api.swagger.Swagger3Processor
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class ApiProcessorDetectorTest {

    @Test
    fun detectApiProcessor() {
        val file = File("src/test/resources/testsets/postman.json")
        val processor = ApiProcessorDetector.detectApiProcessor(file, true)!!
        processor.javaClass shouldBe PostmanProcessor::class.java

        // swagger-3.json
        val file2 = File("src/test/resources/testsets/swagger-3.json")
        val processor2 = ApiProcessorDetector.detectApiProcessor(file2, true)!!
        processor2.javaClass shouldBe Swagger3Processor::class.java

        // swagger-3.yaml
        val file3 = File("src/test/resources/testsets/swagger-3.yaml")
        val processor3 = ApiProcessorDetector.detectApiProcessor(file3, true)!!
        processor3.javaClass shouldBe Swagger3Processor::class.java
    }
}
