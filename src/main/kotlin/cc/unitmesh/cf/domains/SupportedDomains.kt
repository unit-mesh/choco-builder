package cc.unitmesh.cf.domains

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.Serializable

@Serializable
enum class SupportedDomains(@JsonValue val value: String) {
    Frontend("frontend"),
    Ktor("ktor"),
    SQL("sql"),
    Custom("custom"),
    ;
}
