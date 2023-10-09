package cc.unitmesh.code.interpreter.api

import kotlinx.serialization.Serializable

@Serializable
data class InterpreterRequest(
    var id: Int = -1,
    var code: String,
    val language: String = "kotlin",
    val history: Boolean = false,
    var port: Int = 8080
)
