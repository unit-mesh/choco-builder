package cc.unitmesh.cf.core.process

interface DomainDetector {
    fun detect(question: String): List<String>
}
