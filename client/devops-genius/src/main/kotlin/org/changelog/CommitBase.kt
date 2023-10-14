package org.changelog

import kotlinx.serialization.Serializable

@Serializable
data class CommitReference(
    val raw: String,
    val action: String? = null,
    val owner: String? = null,
    val repository: String? = null,
    val issue: String,
    val prefix: String,
)

@Serializable
data class CommitNote(
    var title: String,
    var text: String,
)

@Serializable
data class Commit(
    var merge: String? = null,
    var revert: Map<String, String?>? = null,
    var header: String? = null,
    var body: String? = null,
    var footer: String? = null,
    val notes: List<CommitNote> = emptyList(),
    val mentions: List<String> = emptyList(),
    val references: MutableList<CommitReference> = mutableListOf(),
    var meta: MutableMap<String, String?> = mutableMapOf(),
)




