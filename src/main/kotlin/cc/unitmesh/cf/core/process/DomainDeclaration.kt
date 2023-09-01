package cc.unitmesh.cf.core.process

interface DomainDeclaration {
    fun matchQuestion(question: String): List<String>
}
