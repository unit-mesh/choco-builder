package cc.unitmesh.docs.model

data class RootDocContent(
    val root: DocContent,
    val children: List<DocContent>,
)