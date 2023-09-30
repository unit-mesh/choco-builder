package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class TemplateDatasource {
    @Serializable
    @SerialName("file")
    data class File(val value: String) : TemplateDatasource()
}