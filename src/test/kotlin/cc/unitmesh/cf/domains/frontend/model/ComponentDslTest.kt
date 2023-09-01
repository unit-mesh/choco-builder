package cc.unitmesh.cf.domains.frontend.model

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class ComponentDslTest {
    @Test
    fun test() {
        val resource = ComponentDsl::class.java.getResource("/frontend/components/dialog.json").readText()
        val dsl = Json.decodeFromString<ComponentDsl>(resource)
        dsl.name shouldBe "Dialog"
    }
}