package cc.unitmesh.cf.infrastructure.repository

import cc.unitmesh.cf.infrastructure.cache.EmbeddingCache
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmbeddingCacheRepository : JpaRepository<EmbeddingCache, String> {
    fun findByText(text: String): EmbeddingCache?
}
