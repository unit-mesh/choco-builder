package org.changelog

data class CommitReference(
    val raw: String,
    val action: String? = null,
    val owner: String? = null,
    val repository: String? = null,
    val issue: String,
    val prefix: String,
)

data class CommitNote(
    var title: String,
    var text: String,
)

typealias CommitMeta = MutableMap<String, String?>

data class Commit(
    var merge: String? = null,
    var revert: Map<String, String?>? = null,
    var header: String? = null,
    var body: String? = null,
    var footer: String? = null,
    val notes: List<CommitNote> = emptyList(),
    val mentions: List<String> = emptyList(),
    val references: List<CommitReference> = emptyList(),
    var meta: CommitMeta = mutableMapOf(),
)




