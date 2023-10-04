package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.base.TreeDoc
import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class PromptScriptDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    override fun execute(): List<TreeDoc> {
        return processor
            .process(rootDir)
            .map {
                extractRootNode(it)
            }.flatten()
    }

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
                val kDoc = clazz.findKDoc()!!
                val children: List<KDocContent> = if (clazz.isSealed()) {
                    extractSealedClassDoc(clazz)
                } else {
                    listOf()
                }

                docs.add(TreeDoc(kDoc, children))
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
