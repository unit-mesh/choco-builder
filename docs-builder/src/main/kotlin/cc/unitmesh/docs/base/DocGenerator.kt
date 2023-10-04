package cc.unitmesh.docs.base

import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.render.DocHeader
import cc.unitmesh.docs.render.DocPage
import cc.unitmesh.docs.render.DocText
import java.io.File

data class TreeDoc(
    val root: KDocContent,
    val children: List<KDocContent>,
)

abstract class DocGenerator {
    val baseDir = "build" + File.separator

    open fun execute() : List<TreeDoc> {
        return listOf()
    }

    fun stringify(page: DocPage): String {
        var output = ""
        page.content.forEach {
            when (it) {
                is DocText -> {
                    output += "${it.text}\n"
                }

                is DocHeader -> {
                    output += "#".repeat(it.level) + " " + it.title + "\n"
                }
            }
            output += "\n"
        }

        return output
    }
}