package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.DslContext

/**
 * TODO: use Knowledge-base to find related components
 * 1. use OpenAI to generate related components
 * 2. use Knowledge-base to find related components
 */
class FrontendDslContext(
    private val similarLayouts: List<String>,
    private val relatedComponents: List<String>,
) : DslContext(nearestInterpreters = listOf(), chatHistories = "") {
    override fun compileVariable(text: String): String {
        return text
            .replace("{layouts}", similarLayouts.joinToString(separator = ","))
            .replace("{components}", relatedComponents.joinToString(separator = ","))
    }
}