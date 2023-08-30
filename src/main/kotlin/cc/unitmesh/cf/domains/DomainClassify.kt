package cc.unitmesh.cf.domains

import cc.unitmesh.cf.core.process.DomainDetector
import cc.unitmesh.cf.core.process.impl.DomainDetectorPlaceholder
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import org.reflections.Reflections
import org.springframework.stereotype.Component

@Component
class DomainClassify(
    private val cachedEmbedding: CachedEmbedding,
) {
    val cachedDomains: MutableList<Class<out DomainDetector>> = mutableListOf()
    fun dispatch(question: String): DomainDetector {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DomainDetectorPlaceholder()
    }

    fun lookupDomains(): List<Class<out DomainDetector>> {
        if (cachedDomains.isNotEmpty()) {
            return cachedDomains
        }

        val domains = Reflections(DomainClassify::class.java.`package`.name).getSubTypesOf(DomainDetector::class.java)
            .toList()

        this.cachedDomains.addAll(domains)
        return domains
    }
}