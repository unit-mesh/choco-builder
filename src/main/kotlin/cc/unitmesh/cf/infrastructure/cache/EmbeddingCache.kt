package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.utils.Constants
import cc.unitmesh.cf.infrastructure.utils.nextId
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Comment

@Entity
class EmbeddingCache(
    @Id
    val id: String = nextId(),
    @Column(unique = true)
    val text: String,

    @Column(length = Constants.MAX_TOKEN_LENGTH)
    @Convert(converter = DoubleArrayConverter::class)
    val embedding: List<Double>,
)
