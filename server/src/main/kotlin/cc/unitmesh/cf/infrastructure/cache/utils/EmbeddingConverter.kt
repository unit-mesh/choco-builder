package cc.unitmesh.cf.infrastructure.cache.utils

import cc.unitmesh.nlp.embedding.Embedding
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class EmbeddingConverter : AttributeConverter<Embedding, String> {
    override fun convertToDatabaseColumn(attribute: Embedding?): String? {
        return attribute?.joinToString(separator = ",")
    }

    override fun convertToEntityAttribute(dbData: String?): Embedding? {
        if (dbData == null) {
            return null
        }

        val array = dbData.split(",").filter { it.isNotEmpty() }.toTypedArray()
        return array.map { it.toDouble() }
    }
}
