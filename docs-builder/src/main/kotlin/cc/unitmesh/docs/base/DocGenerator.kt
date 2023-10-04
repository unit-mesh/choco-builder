package cc.unitmesh.docs.base

import cc.unitmesh.docs.kdoc.ClassSample
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
    val sampleCode: ClassSample? = null,
) {
    companion object {
        fun fromKDoc(content: KDocContent, sampleCode: ClassSample? = null): DocContent {
            return DocContent(
                name = content.element?.name ?: "",
                content = content.contentTag.getContent(),
                element = content.element,
                sampleCode = sampleCode
            )
        }
    }
}

abstract class DocGenerator {

    open fun execute(): List<TreeDoc> {
        return listOf()
    }
}