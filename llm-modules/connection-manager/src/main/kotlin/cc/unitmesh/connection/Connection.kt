package cc.unitmesh.connection

/**
 * A connection entity that stores the connection information.
 *
 * @param name Connection name
 * @param type Possible values include: "OpenAI", "AzureOpenAI", "Custom".
 * @param configs The configs kv pairs.
 * @param secrets The secrets kv pairs.
 */
abstract class Connection(
    val name: String,
    val type: ConnectionType,
    val configs: Map<String, String> = mapOf(),
    val secrets: Map<String, String> = mapOf(),
) {

}

enum class ConnectionType(val value: String) {
    OpenAI("OpenAI"),
    AzureOpenAI("AzureOpenAI"),
    Custom("Custom")
}
