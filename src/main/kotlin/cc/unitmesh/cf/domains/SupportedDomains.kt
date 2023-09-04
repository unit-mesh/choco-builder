package cc.unitmesh.cf.domains

import kotlinx.serialization.Serializable

@Serializable
enum class SupportedDomains {
    Frontend,
    Ktor,
    SQL,
    Custom,
    ;
}