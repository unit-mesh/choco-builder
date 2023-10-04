package cc.unitmesh.connection

import kotlinx.serialization.Serializable

/**
 * A connection entity that stores the connection information default use `connection.yml` in current directory.
 *
 * For example:
 * ```yaml
 * name: open_ai_connection
 * type: OpenAI
 * secrets:
 *   api-key: ak-xxxx
 * ```
 *
 * current supported connection type:
 *
 * - OpenAI
 * - CustomLlm
 */
@Serializable
open class ConnectionConfig(
    val `$schema`: String = "",
    val name: String,
    val type: ConnectionType,
    private val configs: Map<String, String> = mapOf(),
    /**
     * TODO: Secrets should had some different handle in future
     */
    private val secrets: Map<String, String> = mapOf(),
) {
    fun convert(): ConnectionConfig {
        return when (type) {
            ConnectionType.OpenAI -> {
                val host = configs.getOrDefault("api-host", "")
                val key = secrets.getOrDefault("api-key", "")
                OpenAiConnection(apiHost = host, apiKey = key)
            }

            ConnectionType.AzureOpenAI -> TODO()
            ConnectionType.CustomLlm -> TODO()
            ConnectionType.ExtTool -> TODO()
            ConnectionType.MockLlm -> {
                val response = configs.getOrDefault("api-response", "")
                MockLlmConnection(response = response)
            }
        }
    }
}

enum class ConnectionType(val value: String) {
    OpenAI("OpenAI"),
    AzureOpenAI("AzureOpenAI"),
    CustomLlm("CustomLlm"),
    MockLlm("MockLlm"),
    ExtTool("ExtTool"),
}