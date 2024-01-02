package cc.unitmesh.connection

/**
 * OpenAiConnection is a connection for OpenAI API.
 * For example:
 * ```yaml
 * name: open_ai_connection
 * type: OpenAI
 * secrets:
 *   api-key: ak-xxxx
 *```
 *
 * If you are using proxyed OpenAI API, you can set the api-host in configs.
 *
 * ```yaml
 * name: open_ai_connection
 * type: OpenAI
 * configs:
 *   api-host: https://api.aios.chat/
 * secrets:
 *   api-key: ak-xxxx
 * ```
 */
data class OpenAiConnection(
    val apiHost: String,
    val apiKey: String,
) : ConnectionConfig(
    name = "openai",
    type = ConnectionType.OpenAI
) {

}

data class AzureOpenAiConnection(
    val apiHost: String,
    val apiKey: String,
) : ConnectionConfig(
    name = "azure-openai",
    type = ConnectionType.AzureOpenAI
) {

}

