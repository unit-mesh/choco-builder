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
    val title: String,
    val text: String,
)

typealias CommitMeta = Map<String, String?>

open class CommitBase(
    val merge: String? = null,
    val revert: Map<String, String>? = null,
    val header: String? = null,
    val body: String? = null,
    val footer: String? = null,
    val notes: List<CommitNote>,
    val mentions: List<String>,
    val references: List<CommitReference>,
)

data class Commit(
    val meta: CommitMeta,
) : CommitBase(
    notes = listOf(),
    mentions = listOf(),
    references = listOf(),
)
