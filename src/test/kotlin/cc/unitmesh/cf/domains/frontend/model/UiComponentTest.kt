package cc.unitmesh.cf.domains.frontend.model

import io.kotest.matchers.ints.shouldBeGreaterThan
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class UiComponentTest {
    @Test
    fun test() {
        val resource = UiComponent::class.java.getResource("/frontend/components/element-ui.json").readText()
        val dsls = Json.decodeFromString<List<UiComponent>>(resource)
        dsls.size shouldBeGreaterThan 1
    }
}