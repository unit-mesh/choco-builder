package cc.unitmesh.connection

enum class ConnectionType(val value: String) {
    OpenAI("OpenAI"),
    AzureOpenAI("AzureOpenAI"),
    Custom("Custom")
}
