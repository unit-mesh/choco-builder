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
    val cachedDomains: MutableMap<String, DomainDeclaration> = mutableMapOf()

    fun semanticSearch(question: String): DomainDeclaration {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DomainDeclarationPlaceholder()
    }

    private val packageName = DomainClassify::class.java.`package`.name

    fun lookupDomains(): MutableMap<String, DomainDeclaration> {
        if (cachedDomains.isNotEmpty()) {
            return cachedDomains
        }

        Reflections(packageName)
            .getSubTypesOf(DomainDeclaration::class.java)
            .map {
                val newInstance = Class.forName(it.name).getDeclaredConstructor().newInstance()
                cachedDomains[it.name] = newInstance as DomainDeclaration
            }

        return cachedDomains
    }
}