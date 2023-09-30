package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class StrategyItem {
    @SerialName("connection")
    @Serializable
    data class ConnectionItem(val value: List<Variable>) : StrategyItem()

    @SerialName("repeat")
    @Serializable
    data class RepeatItem(val value: Int) : StrategyItem()
}
