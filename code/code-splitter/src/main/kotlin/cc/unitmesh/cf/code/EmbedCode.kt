package cc.unitmesh.cf.code

import cc.unitmesh.nlp.embedding.Embedding

data class EmbedCode (
    val id: String? = null,
    val lang: String,
    val repoName: String,
    val repoRef: String,
    val codeType: CodeType,
    val relativePath: String,
    val contentHash: String,
    val displayText: String,
    val text: String,
    val startLine: Long,
    val endLine: Long,
    val startByte: Long,
    val endByte: Long,
    val branches: List<String>,
    val embedding: Embedding? = null,
    val score: Float? = null,
)

