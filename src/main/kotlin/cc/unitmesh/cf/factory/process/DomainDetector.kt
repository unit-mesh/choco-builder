package cc.unitmesh.cf.factory.process

interface DomainDetector {
    fun detect(question: String): List<String>
}

class DomainDetectorPlaceholder : DomainDetector {
    override fun detect(question: String): List<String> {
        return listOf()
    }
}