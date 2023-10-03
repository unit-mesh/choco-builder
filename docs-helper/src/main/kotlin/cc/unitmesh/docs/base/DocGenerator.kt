package cc.unitmesh.docs.base

import cc.unitmesh.docs.render.DocHeader
import cc.unitmesh.docs.render.DocPage
import cc.unitmesh.docs.render.DocText
import java.io.File

abstract class DocGenerator {
    val baseDir = "build" + File.separator

    open fun execute() {}

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