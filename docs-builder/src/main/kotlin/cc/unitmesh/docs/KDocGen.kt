package cc.unitmesh.docs

import cc.unitmesh.docs.kdoc.findKDoc
import cc.unitmesh.docs.model.DocContent
import cc.unitmesh.docs.model.DocGenerator
import cc.unitmesh.docs.model.RootDocContent
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class KDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    private var inheritanceDoc = mutableMapOf<String, List<DocContent>>()
    private var fileNodes = listOf<FileASTNode>()

    override fun execute(): List<RootDocContent> {
        fileNodes = processor.process(rootDir)
        return extractNodes(fileNodes)
    }

    fun extractNodes(fileASTNodes: List<FileASTNode>): List<RootDocContent> {
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

            RootDocContent(DocContent.fromKDoc(kDoc), docs)
        }
        .filterNotNull()

    private fun extractRootNode(node: FileASTNode): List<RootDocContent> {
        return node.children()
            .map(::extractChildNode)
            .flatten()
            .toList()
    }

    private fun extractChildNode(node: ASTNode): MutableList<RootDocContent> {
        val docs: MutableList<RootDocContent> = mutableListOf()

        when (node.elementType) {
            KtNodeTypes.CLASS -> {
                val clazz = node.psi as KtClass
                val kDoc = clazz.findKDoc() ?: return docs
                val docContent = DocContent.fromKDoc(kDoc, buildSample(clazz))
                if (clazz.isSealed()) {
                    docs.add(RootDocContent(docContent, extractSealedClassDoc(clazz)))
                }

                if (clazz.superTypeListEntries.isNotEmpty()) {
                    val superName = clazz.superTypeListEntries[0].typeAsUserType?.referencedName ?: ""
                    inheritanceDoc[superName] = inheritanceDoc.getOrPut(superName) { listOf() } + docContent
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
                    (astNode.psi as KtClass).findKDoc()?.let {
                        docs = docs.plus(DocContent.fromKDoc(it))
                    }
                }
            }
        }

        return docs
    }
}


