package cc.unitmesh.connection

import kotlinx.serialization.Serializable

/**
 * A connection entity that stores the connection information.
 *
 * @param name Connection name
 * @param type Possible values include: "OpenAI", "AzureOpenAI", "Custom".
 * @param configs The configs kv pairs, like api_host, api_base, etc.
 * @param secrets The secrets kv pairs, like api_key.
 */
@Serializable
open class BaseConnection(
    val `$schema`: String = "",
    val name: String,
    val type: ConnectionType,
    private val configs: Map<String, String> = mapOf(),
    /**
     * TODO: Secrets should had some different handle in future
     */
    private val secrets: Map<String, String> = mapOf(),
) {
    fun convert(): BaseConnection {
        return when (type) {
            ConnectionType.OpenAI -> {
                val host = configs.getOrDefault("api-host", "")
                val key = secrets.getOrDefault("api-key", "")
                OpenAiConnection(apiHost = host, apiKey = key)
            }

            ConnectionType.AzureOpenAI -> TODO()
            ConnectionType.CustomLlm -> TODO()
            ConnectionType.ExtTool -> TODO()
            ConnectionType.MockLlm -> MockLlmConnection()
        }
    }
}
