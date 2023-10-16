package cc.unitmesh.genius.devops

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val id: String,
    val title: String,
    val body: String,
    val url: String = "",
    val labels: List<String> = listOf(),
    val assignees: List<String> = listOf(),
) {

}
