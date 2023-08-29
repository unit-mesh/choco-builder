package cc.unitmesh.cf.infrastructure.cache

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class DoubleArrayConverter : AttributeConverter<List<Double>, String> {
    override fun convertToDatabaseColumn(attribute: List<Double>?): String? {
        return attribute?.joinToString(separator = ",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<Double>? {
        if (dbData == null) {
            return null
        }
        val array = dbData.split(",").filter { it.isNotEmpty() }.toTypedArray()
        return array.map { it.toDouble() }
    }
}
