package cc.unitmesh.cf.domains.frontend.context

data class FEVariables(
    val question: String,
    val histories: List<String>,
    val layouts: String,
    val components: String,
)