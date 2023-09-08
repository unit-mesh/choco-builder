package cc.unitmesh.cf.core.flow

interface DomainDeclaration {
    /**
     * A domain name should be unique and lower case, also need to be human readable.
     */
    val domainName: String
    val description: String
    fun workflow(question: String): Workflow
}
