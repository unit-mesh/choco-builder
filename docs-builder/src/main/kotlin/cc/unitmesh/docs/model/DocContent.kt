package cc.unitmesh.docs.model

import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.sample.ClassSample
import org.jetbrains.kotlin.psi.KtElement

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