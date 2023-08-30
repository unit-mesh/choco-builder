package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.Domain
import cc.unitmesh.cf.core.process.DomainDetector

@Domain(name = "frontend", description = "use for create frontend component, page, etc.")
class FrontendDomainDetector: DomainDetector {
    override fun detect(question: String): List<String> {
        return listOf()
    }
}