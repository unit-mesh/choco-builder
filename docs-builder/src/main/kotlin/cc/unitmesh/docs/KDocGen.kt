package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocContent
import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.base.TreeDoc
import cc.unitmesh.docs.kdoc.ClassSample
import cc.unitmesh.docs.kdoc.FunctionSample
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class KDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    private var inheritanceDoc = mutableMapOf<String, List<DocContent>>()
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
            val kDoc = clazz.findKDoc() ?: return@map null

            TreeDoc(DocContent.fromKDoc(kDoc), docs)
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
                val kDoc = clazz.findKDoc() ?: return docs
                val docContent = DocContent.fromKDoc(kDoc, buildSample(clazz))
                if (clazz.isSealed()) {
                    val children = extractSealedClassDoc(clazz)
                    docs.add(TreeDoc(docContent, children))
                }

                if (clazz.superTypeListEntries.isNotEmpty()) {
                    val superName = clazz.superTypeListEntries[0].typeAsUserType?.referencedName ?: ""
                    val doc = docContent
                    inheritanceDoc[superName] = inheritanceDoc.getOrPut(superName) { listOf() }.plus(doc)
                }
            }
        }

        return docs
    }

    private fun extractSealedClassDoc(clazz: KtClass): List<DocContent> {
        var docs: List<DocContent> = listOf()
        val body = clazz.body ?: return docs

        body.node.children().forEach { astNode ->
            when (astNode.elementType) {
                KtNodeTypes.CLASS -> {
                    val child = astNode.psi as KtClass
                    child.findKDoc()?.let {
                        docs = docs.plus(DocContent.fromKDoc(it))
                    }
                }
            }
        }

        return docs
    }

    fun buildSample(node: KtElement): ClassSample? {
        val testClass = classNodes.find { it.name == "${node.name}Test" || it.name == "${node.name}Tests" }
        if (testClass == null) {
            return null
        }

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


