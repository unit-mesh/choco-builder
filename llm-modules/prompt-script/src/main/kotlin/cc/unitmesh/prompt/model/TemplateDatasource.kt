package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TemplateDatasource is the job's template datasource config, which will be used for render template.
 * The datasource can be a file, a directory or an http url, or a string, which be auto loaded by extension.
 * For example:
 *
 * ```yaml
 * template-datasource:
 *    - type: file
 *      value: datasource.json
 * ```
 *
 * We will load the datasource.json file as the template datasource.
 */
@Serializable
sealed class TemplateDatasource {
    /**
     * File is a file datasource, which will load data from a file.
     */
    @Serializable
    @SerialName("file")
    data class File(val value: String) : TemplateDatasource()
}