package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.prompt.Workflow

interface DomainDeclaration {
    /**
     * A domain name should be unique and lower case, also need to be human readable.
     */
    val domainName: String
    val description: String
    fun workflow(question: String): Workflow
}

/**
 * 子域用于根据用户的问题，定位到对应的子领域，如：用户想编写一个前端页面，那么子域就是前端页面。
 */
interface SubDomainDeclaration {
}
