package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JobStrategy is the job's strategy config, which will be used for [cc.unitmesh.prompt.model.Job].
 * The strategy can be a connection config or a repeat config or others.
 * For example:

 * ```yaml
 * strategy:
 *    - type: connection
 *      value:
 *        - type: range
 *          key: temperature
 *          range: 0.7~1.0
 *          step: 0.1
 *    - type: repeat
 *      value: 3
 * ```
 */
@Serializable
sealed class JobStrategy {
    /**
     * Connection is a config of [cc.unitmesh.connection.ConnectionConfig],
     * which will be used for [cc.unitmesh.openai.LlmProvider]
     * like temperature, top-p, top-k, presence_penalty, frequency_penalty, stop etc.
     * for example:
     *
     *```yaml
     * - type: connection
     *   value:
     *     - type: range
     *       key: temperature
     *       range: 0.7~1.0
     *       step: 0.1
     *```
     *
     */
    @SerialName("connection")
    @Serializable
    data class Connection(val value: List<Variable>) : JobStrategy()

    /**
     * Repeat is a config of repeat times.
     * for example:
     *
     *```yaml
     * - type: repeat
     *   value: 3
     *```
     */
    @SerialName("repeat")
    @Serializable
    data class Repeat(val value: Int) : JobStrategy()

    /**
     * Repeat is a config of repeat times.
     * for example:
     *
     *```yaml
     * - type: jsonl
     *```
     */
    @SerialName("datasource-collection")
    @Serializable
    data class DatasourceCollection(val value: List<Variable>) : JobStrategy()
}
