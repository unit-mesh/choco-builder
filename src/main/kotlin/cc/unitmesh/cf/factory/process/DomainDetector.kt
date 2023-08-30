package cc.unitmesh.cf.factory.process

import cc.unitmesh.cf.core.Domain

interface DomainDetector {
    fun detect(question: String): List<String>
}

@Domain(name = "默认问题检查器", description = "根据问题判断领域")
class DomainDetectorPlaceholder : DomainDetector {
    override fun detect(question: String): List<String> {
        return listOf()
    }
}