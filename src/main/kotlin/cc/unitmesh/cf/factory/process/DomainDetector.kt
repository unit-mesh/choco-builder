package cc.unitmesh.cf.factory.process

interface DomainDetector {
    fun detect(question: String): List<String>
}