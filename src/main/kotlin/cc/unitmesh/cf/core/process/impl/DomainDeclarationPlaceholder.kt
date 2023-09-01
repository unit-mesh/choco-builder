package cc.unitmesh.cf.core.process.impl

import cc.unitmesh.cf.core.Domain
import cc.unitmesh.cf.core.process.DomainDeclaration
import org.springframework.stereotype.Component

@Component
class DomainDeclarationPlaceholder : DomainDeclaration {
    override val name: String get() = "问题检查器占位"
    override val description: String get() = "根据问题判断领域"


    override fun matchQuestion(question: String): List<String> {
        return listOf()
    }
}