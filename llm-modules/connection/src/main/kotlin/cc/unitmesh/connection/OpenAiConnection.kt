package cc.unitmesh.connection

data class OpenAiConnection(
    val apiHost: String,
    val apiKey: String,
) : Connection(
    name = "openai",
    type = ConnectionType.OpenAI
) {

}