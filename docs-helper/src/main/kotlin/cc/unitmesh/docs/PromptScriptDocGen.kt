package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import com.intellij.lang.ASTNode
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path

class PromptScriptDocGen(val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()

    override fun execute() {
        val lists = processor
            .process(rootDir)
            .forEach {
                it.children().forEach { child ->
                    extractDocRecursively(child)
                }
            }
    }

    private fun extractDocRecursively(node: ASTNode) {
        when (node.elementType) {
            KtNodeTypes.CLASS -> {
                val clazz = node.psi as KtClass
                clazz.getSuperTypeList()?.entries?.forEach {
                    println(it.typeAsUserType?.referencedName)
                }

                if (clazz.isSealed()) {
                    extractSealedClassDoc(clazz)
                }
            }

            KDocTokens.KDOC -> {
                println(node.text)
            }

            else -> {
//                println(node.elementType)
            }
        }
    }

    private fun extractSealedClassDoc(clazz: KtClass) {
        val body = clazz.body ?: return
        body.node.children().forEach { astNode ->
            when (astNode.elementType) {
                KtNodeTypes.CLASS -> {
                    val child = astNode.psi as KtClass
                    child.findKDoc()?.let {
                        it.sections.forEach { section ->
                            println(section.getContent())
                        }
                    }
                }
            }
        }
    }
}
