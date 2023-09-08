package cc.unitmesh.cf.domains

import cc.unitmesh.cf.core.flow.DomainDeclaration
import cc.unitmesh.cf.infrastructure.cache.CachedEmbeddingService
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import org.springframework.stereotype.Component


@Component
class DomainClassify(private val cachedEmbedding: CachedEmbeddingService) {
    val cachedDomains: MutableMap<String, DomainDeclaration> = mutableMapOf()

    private val packageName = DomainClassify::class.java.`package`.name

    fun lookupDomains(): MutableMap<String, DomainDeclaration> {
        if (cachedDomains.isNotEmpty()) {
            return cachedDomains
        }

        Reflections(ConfigurationBuilder().forPackages(packageName))
            .getSubTypesOf(DomainDeclaration::class.java)
            .map {
                val newInstance = Class.forName(it.name).getDeclaredConstructor().newInstance() as DomainDeclaration
                cachedDomains[newInstance.domainName] = newInstance
            }

        return cachedDomains
    }
}