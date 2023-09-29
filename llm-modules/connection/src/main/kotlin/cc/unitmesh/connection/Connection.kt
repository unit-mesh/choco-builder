package cc.unitmesh.connection

import kotlinx.serialization.Serializable

/**
 * A connection entity that stores the connection information.
 *
 * @param name Connection name
 * @param type Possible values include: "OpenAI", "AzureOpenAI", "Custom".
 * @param configs The configs kv pairs.
 * @param secrets The secrets kv pairs.
 */
@Serializable
open class Connection(
    val name: String,
    val type: ConnectionType,
    private val configs: Map<String, String> = mapOf(),
    private val secrets: Map<String, String> = mapOf(),
) {
    fun convert(): Connection {
        return when (type) {
            ConnectionType.OpenAI -> {
                val host = configs.getOrDefault("api-host", "")
                val key = secrets.getOrDefault("api-key", "")
                OpenAiConnection(apiHost = host, apiKey = key)
            }

            ConnectionType.AzureOpenAI -> TODO()
            ConnectionType.Custom -> TODO()
        }
    }
}
