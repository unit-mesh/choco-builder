package cc.unitmesh.cf.domains

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.infrastructure.cache.CachedEmbeddingService
import org.reflections.Reflections
import org.springframework.stereotype.Component


@Component
class DomainClassify(private val cachedEmbedding: CachedEmbeddingService, ) {
    val cachedDomains: MutableMap<String, DomainDeclaration> = mutableMapOf()

//    fun semanticSearch(question: String): DomainDeclaration {
//        val question: Embedding = cachedEmbedding.createEmbedding(question)
//        return DomainDeclarationPlaceholder()
//    }

    private val packageName = DomainClassify::class.java.`package`.name

    fun lookupDomains(): MutableMap<String, DomainDeclaration> {
        if (cachedDomains.isNotEmpty()) {
            return cachedDomains
        }

        Reflections(packageName)
            .getSubTypesOf(DomainDeclaration::class.java)
            .map {
                val newInstance = Class.forName(it.name).getDeclaredConstructor().newInstance() as DomainDeclaration
                cachedDomains[newInstance.domainName] = newInstance
            }

        return cachedDomains
    }
}