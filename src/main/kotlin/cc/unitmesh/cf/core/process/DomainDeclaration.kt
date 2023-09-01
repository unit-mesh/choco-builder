package cc.unitmesh.cf.core.process

interface DomainDeclaration {
    val name: String
    val description: String
    fun matchQuestion(question: String): List<String>
}
