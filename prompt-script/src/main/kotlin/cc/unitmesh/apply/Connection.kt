package cc.unitmesh.apply

/**
 * for create LlmProvider
 */
class Connection(connectionType: ConnectionType) {
    fun prompt(function: (str: String) -> String): String {
        return function("")
    }
}

enum class ConnectionType {
    OpenAI
}