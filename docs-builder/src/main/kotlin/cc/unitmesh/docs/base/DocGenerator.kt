package cc.unitmesh.docs.base

import cc.unitmesh.docs.kdoc.KDocContent
import org.jetbrains.kotlin.psi.KtElement

data class TreeDoc(
    val root: DocContent,
    val children: List<DocContent>,
)

data class DocContent(
    val name: String,
    val content: String,
    val element: KtElement?,
) {
    companion object {
        fun fromKDoc(content: KDocContent): DocContent {
            return DocContent(
                name = content.element?.name ?: "",
                content = content.contentTag.getContent(),
                element = content.element
            )
        }
    }
}

abstract class DocGenerator {

    open fun execute(): List<TreeDoc> {
        return listOf()
    }
}