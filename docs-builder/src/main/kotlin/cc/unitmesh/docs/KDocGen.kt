package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.base.TreeDoc
import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.kdoc.SampleCode
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class KDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    private var inheritanceDoc = mutableMapOf<String, List<KDocContent>>()
    private var classNodes = listOf<KtClass>()
    private var fileNodes = listOf<FileASTNode>()

    override fun execute(): List<TreeDoc> {
        fileNodes = processor.process(rootDir)
        return extractNodes(fileNodes)
    }

    fun extractNodes(fileASTNodes: List<FileASTNode>): List<TreeDoc> {
        classNodes = fileASTNodes.flatMap { node ->
            node.children()
                .filter { it.elementType == KtNodeTypes.CLASS }
                .mapNotNull { it.psi as? KtClass }
        }

        val normalDoc = fileASTNodes
            .map {
                extractRootNode(it)
            }.flatten()

        val interfaceDocs = buildInterfaceDocs()

        return normalDoc + interfaceDocs
    }

    private fun buildInterfaceDocs() = inheritanceDoc
        .filter { it.value.isNotEmpty() }
        .map { (name, docs) ->
            val ktClass = classNodes.find { it.name == name }
            val clazz = ktClass ?: return@map null
            val kDoc = clazz.buildDocWithTest(classNodes) ?: return@map null

            TreeDoc(kDoc, docs)
        }
        .filterNotNull()

    private fun extractRootNode(node: FileASTNode): List<TreeDoc> {
        return node.children()
            .map(::extractChildNode)
            .flatten()
            .toList()
    }

    private fun extractChildNode(node: ASTNode): MutableList<TreeDoc> {
        val docs: MutableList<TreeDoc> = mutableListOf()

        when (node.elementType) {
            KtNodeTypes.CLASS -> {
                val clazz = node.psi as KtClass
                val kDoc = clazz.buildDocWithTest(classNodes) ?: return docs
                if (clazz.isSealed()) {
                    val children = extractSealedClassDoc(clazz)
                    docs.add(TreeDoc(kDoc, children))
                }

                if (clazz.superTypeListEntries.isNotEmpty()) {
                    val superName = clazz.superTypeListEntries[0].typeAsUserType?.referencedName ?: ""
                    inheritanceDoc[superName] = inheritanceDoc.getOrPut(superName) { listOf() }.plus(kDoc)
                }
            }
        }

        return docs
    }

    private fun extractSealedClassDoc(clazz: KtClass): List<KDocContent> {
        var docs: List<KDocContent> = listOf()
        val body = clazz.body ?: return docs

        body.node.children().forEach { astNode ->
            when (astNode.elementType) {
                KtNodeTypes.CLASS -> {
                    val child = astNode.psi as KtClass
                    child.buildDocWithTest(classNodes)?.let {
                        docs = docs.plus(it)
                    }
                }
            }
        }

        return docs
    }
}

fun KtElement.buildDocWithTest(classNodes: List<KtClass>): KDocContent? {
    val kDocContent = this.findKDoc()

    val testClass = classNodes.find { it.name == "${this.name}Test" || it.name == "${this.name}Tests" }
    if (testClass != null) {
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

                sampleCodeValue.name + "\n\n" + linesResults
            }

        }

    }

    return kDocContent
}

private fun getSampleCodeValue(method: KtFunction): SampleCode {
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
                        if (node.text == "name") {
                            val byType = astNode.findChildByType(KtNodeTypes.STRING_TEMPLATE)
                            name = byType?.psi?.let {
                                (it as KtStringTemplateExpression).text
                            } ?: ""
                        }
                        if (node.text == "content") {
                            val contentType = astNode.findChildByType(KtNodeTypes.STRING_TEMPLATE)
                            description = contentType?.text.orEmpty()
                        }
                    }
                }
            }
        }
    }

    return SampleCode(name, description)
}
