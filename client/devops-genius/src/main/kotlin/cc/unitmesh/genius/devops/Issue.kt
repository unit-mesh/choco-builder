package cc.unitmesh.genius.devops

class Issue(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val labels: List<String>,
    val assignees: List<String>,
    val milestone: String,
) {

}
