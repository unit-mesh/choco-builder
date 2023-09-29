package cc.unitmesh.connection

enum class ConnectionType(val value: String) {
    OpenAI("OpenAI"),
    AzureOpenAI("AzureOpenAI"),
    CustomLlm("CustomLlm"),
    ExtTool("ExtTool")
}
