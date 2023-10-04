package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.kdoc.KDocContent
import com.pinterest.ktlint.KtFileProcessor
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path

class PromptScriptDocGen(val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()

    override fun execute() {
        val lists = processor
            .process(rootDir)
            .map {
                extractRootNode(it)
            }
    }

    fun extractRootNode(it: FileASTNode) {
        it.children().forEach { child ->
            extractChildNode(child)
        }
    }

    fun extractChildNode(node: ASTNode) {
        when (node.elementType) {
            KtNodeTypes.CLASS -> {
                val clazz = node.psi as KtClass
                clazz.getSuperTypeList()?.entries?.forEach {
                    println(it.typeAsUserType?.referencedName)
                }

                if (clazz.isSealed()) {
                    val classDoc = extractSealedClassDoc(clazz)
                    println(classDoc)
                }
            }
        }
    }

    fun extractSealedClassDoc(clazz: KtClass): List<KDocContent> {
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
