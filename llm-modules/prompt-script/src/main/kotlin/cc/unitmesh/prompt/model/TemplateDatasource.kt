package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TemplateDatasource is the job's template datasource config, which will be used for render template.
 * The datasource can be a file, a directory or an http url, or a string.
 */
@Serializable
sealed class TemplateDatasource {
    @Serializable
    @SerialName("file")
    data class File(val value: String) : TemplateDatasource()
}