package cc.unitmesh.cf.core.process.impl

import cc.unitmesh.cf.core.Domain
import cc.unitmesh.cf.core.process.DomainDetector

@Domain(name = "问题检查器占位", description = "根据问题判断领域")
class DomainDetectorPlaceholder : DomainDetector {
    override fun detect(question: String): List<String> {
        return listOf()
    }
}