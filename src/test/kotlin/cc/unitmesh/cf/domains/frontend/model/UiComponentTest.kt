package cc.unitmesh.cf.domains.frontend.model

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class UiComponentTest {
    @Test
    fun test() {
        val resource = UiComponent::class.java.getResource("/frontend/components/dialog.json").readText()
        val dsl = Json.decodeFromString<UiComponent>(resource)
        dsl.name shouldBe "Dialog"
    }
}