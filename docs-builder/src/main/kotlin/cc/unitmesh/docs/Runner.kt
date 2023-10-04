package cc.unitmesh.docs

import cc.unitmesh.docs.base.TreeDoc
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.nio.file.Path

class Runner : CliktCommand() {
    val dir by option("-d", "--dir", help = "The directory to process").default(".")

    override fun run() {
        val rootDir = Path.of(dir).toAbsolutePath().normalize()

        // the prompt script parts
        val promptScriptDir = rootDir.resolve("llm-modules/prompt-script")
        val treeDocs = PromptScriptDocGen(promptScriptDir).execute()
        val docs = renderDocs(treeDocs)
        docs.joinToString("\n") { it }.also { println(it) }
    }

    private fun renderDocs(treeDocs: List<TreeDoc>) : List<String> {
        return treeDocs.map { treeDoc ->
            var output = ""
            val root = treeDoc.root
            val children = treeDoc.children

            output += "## ${root.element?.name} \n\n> ${root.contentTag.getContent()}\n\n"
            children.forEach { child ->
                output += "- ${child.element?.name}. ${child.contentTag.getContent()}\n"
            }

            output
        }
    }
}

fun main(args: Array<String>) = Runner().main(args)

