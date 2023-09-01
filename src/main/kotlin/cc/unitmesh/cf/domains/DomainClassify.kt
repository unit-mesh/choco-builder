package cc.unitmesh.cf.domains

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.process.impl.DomainDeclarationPlaceholder
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import org.reflections.Reflections
import org.springframework.stereotype.Component

@Component
class DomainClassify(
    private val cachedEmbedding: CachedEmbedding,
) {
    val cachedDomains: MutableList<Class<out DomainDeclaration>> = mutableListOf()
    fun dispatch(question: String): DomainDeclaration {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DomainDeclarationPlaceholder()
    }

    fun lookupDomains(): List<Class<out DomainDeclaration>> {
        if (cachedDomains.isNotEmpty()) {
            return cachedDomains
        }

        val domains = Reflections(DomainClassify::class.java.`package`.name).getSubTypesOf(DomainDeclaration::class.java)
            .toList()

        this.cachedDomains.addAll(domains)
        return domains
    }
}