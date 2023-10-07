package cc.unitmesh.docs

import cc.unitmesh.docs.model.RootDocContent
import cc.unitmesh.docs.render.CustomJekyllFrontMatter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.nio.file.Path

class Runner : CliktCommand() {
    private val dir by option("-d", "--dir", help = "The directory to process").default(".")

    override fun run() {
        val rootDir = Path.of(dir).toAbsolutePath().normalize()

        processRagScript(rootDir)
        processPromptScript(rootDir)
    }

    private val warningLog =
        "\n{: .warning }\nAutomatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes."

    private fun processRagScript(rootDir: Path) {
        // the rag script parts
        val ragScriptDir = rootDir.resolve("rag-modules/rag-script")
        val ragScriptDocs = KDocGen(ragScriptDir).execute()

        val docs = renderDocs(ragScriptDocs)
        val outputDir = rootDir.resolve("docs/rag-script")
        var index = 10
        docs.forEach { (name, content) ->
            val permalink = uppercaseToDash(name)
            var output = CustomJekyllFrontMatter(name, "RAG Script", index, "/rag-script/$permalink")
                .toMarkdown()

            output = "$output$warningLog"

            val outputFile = outputDir.resolve("$permalink.md")
            outputFile.toFile().writeText(output + "\n\n" + content)
            index += 1
        }
    }

    private fun processPromptScript(rootDir: Path) {
        // the prompt script parts
        val connectionDir = rootDir.resolve("llm-modules/connection")
        val connectionDocs = KDocGen(connectionDir).execute()

        val promptScriptDir = rootDir.resolve("llm-modules/prompt-script")
        val treeDocs = KDocGen(promptScriptDir).execute()

        val docs = renderDocs(treeDocs + connectionDocs)
        val outputDir = rootDir.resolve("docs/prompt-script")
        var index = 10
        docs.forEach { (name, content) ->
            val permalink = uppercaseToDash(name)
            var output = CustomJekyllFrontMatter(name, "Prompt Script", index, "/prompt-script/$permalink")
                .toMarkdown()

            output = "$output$warningLog"

            val outputFile = outputDir.resolve("$permalink.md")
            outputFile.toFile().writeText(output + "\n\n" + content)
            index += 1
        }
    }


    private fun renderDocs(rootDocContents: List<RootDocContent>): Map<String, String> {
        return rootDocContents.associate { treeDoc ->
            val output = StringBuilder()
            val root = treeDoc.root
            val children = treeDoc.children
            val rootFileName = root.element?.containingFile?.name ?: "unknown"
            println("doc root: $rootFileName")

            output.append("# ${root.element?.name} \n\n> ${root.content}\n\n")
            children.forEach { child ->
                output.append("## ${child.element?.name} \n\n")
                output.append("${child.content}\n\n")

                if (child.sampleCode != null) {
                    child.sampleCode.samples.map { sample ->
                        output.append("Sample: ${sample.name}\n\n")
                        output.append("```kotlin\n")
                        output.append("${sample.code}\n")
                        output.append("```\n\n")
                    }
                }
            }

            root.element!!.name!! to output.toString()
        }
    }

    companion object {
        fun uppercaseToDash(name: String): String {
            val result = StringBuilder()

            for (char in name) {
                if (char.isUpperCase() && result.isNotEmpty()) {
                    result.append('-')
                }
                result.append(char.lowercase())
            }

            return result.toString()
        }
    }
}

fun main(args: Array<String>) = Runner().main(args)

