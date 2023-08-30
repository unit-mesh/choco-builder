package cc.unitmesh.cf.infrastructure.llms.model

enum class ChatRole(val value: String) {
    System("system"),
    User("user"),
    Assistant("assistant"),
    Function("function"),
    ;

    companion object
}
