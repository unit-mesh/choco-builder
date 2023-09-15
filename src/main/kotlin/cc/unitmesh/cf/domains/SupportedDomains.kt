package cc.unitmesh.cf.domains

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.Serializable

@Serializable
enum class SupportedDomains(@JsonValue val value: String) {
    Frontend("frontend"),
    CodeInterpreter("code-interpreter"),
    Testcase("testcase"),
    SQL("sql"),
    Spec("spec"),
    CodeSemanticSearch("semantic-search"),
    Custom("custom"),
    ;
}
