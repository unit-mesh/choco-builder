package cc.unitmesh.cf.domains

import cc.unitmesh.cf.core.Domain
import cc.unitmesh.cf.factory.process.DomainDetector
import cc.unitmesh.cf.factory.process.DomainDetectorPlaceholder
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import org.springframework.stereotype.Component
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner

@Component
class DomainDispatcher(
    private val cachedEmbedding: CachedEmbedding,
) {
    fun dispatch(question: String): DomainDetector {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DomainDetectorPlaceholder()
    }

    fun lookupDomains(): List<Class<out DomainDetector>> {
        val reflections = Reflections(DomainDispatcher::class.java.`package`.name, SubTypesScanner(false))

        return reflections.getSubTypesOf(DomainDetector::class.java)
            .toList()
    }
}