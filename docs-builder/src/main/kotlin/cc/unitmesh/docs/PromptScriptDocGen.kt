package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.base.TreeDoc
import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.kdoc.psi.impl.KDocTag
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class PromptScriptDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    private var inheritanceDoc = mutableMapOf<String, List<KDocContent>>()
    private var classNodes = listOf<KtClass>()

    override fun execute(): List<TreeDoc> {
        val nodes = processor.process(rootDir)

        val normalDoc = nodes
            .map {
                extractRootNode(it)
            }.flatten()

        val interfaceDocs = buildInterfaceDocs()

        return normalDoc + interfaceDocs
    }

    fun buildInterfaceDocs() = inheritanceDoc
        .filter { it.value.isNotEmpty() }
        .map { (name, docs) ->
            val clazz = classNodes.find { it.name == name } ?: return@map null
            val kDoc = clazz.findKDoc() ?: return@map null

            TreeDoc(kDoc, docs)
        }
        .filterNotNull()

    fun extractRootNode(node: FileASTNode): List<TreeDoc> {
        return node.children()
            .map(::extractChildNode)
            .flatten()
            .toList()
    }

    fun extractChildNode(node: ASTNode): MutableList<TreeDoc> {
        val docs: MutableList<TreeDoc> = mutableListOf()

        when (node.elementType) {
            KtNodeTypes.CLASS -> {
                val clazz = node.psi as KtClass
                classNodes = classNodes.plus(clazz)

                val kDoc = clazz.findKDoc() ?: return docs
                if (clazz.isSealed()) {
                    val children = extractSealedClassDoc(clazz)
                    docs.add(TreeDoc(kDoc, children))
                }

                if (clazz.superTypeListEntries.isNotEmpty()) {
                    inheritanceDoc.getOrPut(clazz.name ?: "") { listOf(kDoc) }.plus(kDoc)
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
                    child.findKDoc()?.let {
                        docs = docs.plus(it)
                    }
                }
            }
        }

        return docs
    }
}
