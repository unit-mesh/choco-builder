package cc.unitmesh.docs.render

data class CustomJekyllFrontMatter(
    var title: String,
    val parent: String,
    val navOrder: Int,
    val permalink: String,
) {
    fun toMarkdown(): String {
        return """
---
layout: default
title: $title
parent: $parent
nav_order: $navOrder
permalink: $permalink
---

""".trimIndent()
    }
}