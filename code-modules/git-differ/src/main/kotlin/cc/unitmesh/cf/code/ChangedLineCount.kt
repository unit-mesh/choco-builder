package cc.unitmesh.cf.code

import kotlinx.serialization.Serializable

@Serializable
data class ChangedLineCount(
    val added: Int,
    val deleted: Int,
)