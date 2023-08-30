package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.DslContext

class FrontendDslContext(
    /**
     * TODO: use Knowledge-base to find similar layouts
     * 1. use OpenAI to generate similar layouts
     * 2. use Knowledge-base to find similar layouts
     */
    private val similarLayouts: List<String>,
    /**
     * TODO: use Knowledge-base to find related components
     * 1. use OpenAI to generate related components
     * 2. use Knowledge-base to find related components
     */
    private val relatedComponents: List<String>,
) : DslContext(nearestInterpreters = listOf(), chatHistories = "") {
    fun expansion(text: String): String {
        return text
            .replace("{#layouts}", similarLayouts.joinToString(separator = ","))
            .replace("{#components}", relatedComponents.joinToString(separator = ","))
    }
}