package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.cache.utils.EmbeddingConverter
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import cc.unitmesh.cf.infrastructure.utils.Constants
import cc.unitmesh.cf.infrastructure.utils.nextId

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Comment

@Entity
@Comment("Embedding 模型缓存")
class EmbeddingCache(
    @Id
    val id: String = nextId(),

    @Column(unique = true)
    @Comment("Embedding 文本")
    val text: String,

    @Column(length = Constants.MAX_TOKEN_LENGTH)
    @Convert(converter = EmbeddingConverter::class)
    @Comment("Vector Embedding")
    val embedding: Embedding,
)
