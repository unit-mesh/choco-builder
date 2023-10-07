package cc.unitmesh.docs.model

import cc.unitmesh.docs.sample.ClassSample
import cc.unitmesh.docs.sample.FunctionSample
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.psiUtil.children

abstract class DocGenerator {
    var classNodes = listOf<KtClass>()

    open fun execute(): List<RootDocContent> {
        return listOf()
    }

    fun buildSample(node: KtElement): ClassSample? {
        val testClass =
            classNodes.find { it.name == "${node.name}Test" || it.name == "${node.name}Tests" } ?: return null

        val classBody = testClass.body ?: return null
        val methods: List<KtFunction> = classBody.node.children()
            .filter { it.elementType == KtNodeTypes.FUN }
            .map { it.psi as KtFunction }
            .filter {
                it.modifierList?.text?.contains("@SampleCode") ?: false
            }
            .toList()

        // the code block will begin with `// start-sample` and end with `// end-sample`
        val sourceCodes = methods.mapNotNull { method ->
            val sampleCodeValue = getSampleCodeValue(method)

            val lines = method.text.split("\n")
            // find the start line with regex
            val startLine = lines.indexOfFirst { it.contains("// start-sample") }
            // find the end line with regex
            val endLine = lines.indexOfFirst { it.contains("// end-sample") }
            // get the code block
            if (startLine == -1 || endLine == -1) {
                null
            } else {
                val linesResults = lines.subList(startLine + 1, endLine).joinToString("\n") {
                    it.replaceFirst(Regex("^\\s+"), "")
                }

                sampleCodeValue.code = linesResults
                sampleCodeValue
            }
        }

        return ClassSample(node.name!!, sourceCodes)
    }

    private fun getSampleCodeValue(method: KtFunction): FunctionSample {
        // get modifier value from `@SampleCode(name = "检验成功", content = "")`
        var name = ""
        var description = ""
        method.modifierList?.annotationEntries?.filter {
            it.shortName?.asString() == "SampleCode"
        }?.mapNotNull { annotation ->
            annotation.valueArguments.forEach { valueArgument ->
                val astNode = valueArgument.asElement().node
                astNode.children().forEach { node ->
                    when (node.elementType) {
                        KtNodeTypes.VALUE_ARGUMENT_NAME -> {
                            when (node.text) {
                                "name" -> {
                                    val byType = astNode.findChildByType(KtNodeTypes.STRING_TEMPLATE)
                                    name = byType?.text.orEmpty().removeSurrounding("\"")
                                }

                                "content" -> {
                                    val contentType = astNode.findChildByType(KtNodeTypes.STRING_TEMPLATE)
                                    description = contentType?.text.orEmpty().removeSurrounding("\"")
                                }
                            }
                        }
                    }
                }
            }
        }

        return FunctionSample(name, description)
    }
}