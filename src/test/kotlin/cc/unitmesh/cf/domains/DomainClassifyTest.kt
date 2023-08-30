package cc.unitmesh.cf.domains

import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DomainClassifyTest {

    @MockK
    lateinit var cachedEmbedding: CachedEmbedding

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks

    @Test
    fun should_enable_lookup_domains() {
        val domainClassify = DomainClassify(cachedEmbedding)
        val domain = domainClassify.lookupDomains()

        domain.size shouldBeGreaterThanOrEqual   1
    }
}