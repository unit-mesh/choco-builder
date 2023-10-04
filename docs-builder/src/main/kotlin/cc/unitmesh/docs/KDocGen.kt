package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import cc.unitmesh.docs.base.TreeDoc
import cc.unitmesh.docs.kdoc.KDocContent
import cc.unitmesh.docs.kdoc.findKDoc
import com.intellij.lang.ASTNode
import com.intellij.lang.FileASTNode
import com.pinterest.ktlint.KtFileProcessor
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.psiUtil.children
import java.nio.file.Path


class KDocGen(private val rootDir: Path) : DocGenerator() {
    private val processor: KtFileProcessor = KtFileProcessor()
    private var inheritanceDoc = mutableMapOf<String, List<KDocContent>>()
    private var classNodes = listOf<KtClass>()
    private var fileNodes = listOf<FileASTNode>()

    override fun execute(): List<TreeDoc> {
        fileNodes = processor.process(rootDir)

        val normalDoc = fileNodes
            .map {
                extractRootNode(it)
            }.flatten()

        val interfaceDocs = buildInterfaceDocs()

        return normalDoc + interfaceDocs
    }

    @TestOnly
    fun buildInterfaceDocs() = inheritanceDoc
        .filter { it.value.isNotEmpty() }
        .map { (name, docs) ->
            val ktClass = classNodes.find { it.name == name }
            val clazz = ktClass ?: return@map null
            val kDoc = clazz.buildDocWithTest(classNodes) ?: return@map null

            TreeDoc(kDoc, docs)
        }
        .filterNotNull()

    fun extractRootNode(node: FileASTNode): List<TreeDoc> {
        classNodes += node.children()
            .filter { it.elementType == KtNodeTypes.CLASS }
            .mapNotNull { it.psi as? KtClass }

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
    val testClass = classNodes.find { it.name == "${this.name}Test" || it.name == "${this.name}Tests" }
    if (testClass != null) {
        println("find test class: ${testClass.name}")
    }

    return this.findKDoc()
}