package cc.unitmesh.connection

data class OpenAiConnection(
    val apiHost: String,
    val apiKey: String,
) : BaseConnection(
    name = "openai",
    type = ConnectionType.OpenAI
) {

}