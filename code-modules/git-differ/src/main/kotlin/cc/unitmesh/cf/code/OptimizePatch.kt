package cc.unitmesh.cf.code

import kotlinx.serialization.Serializable
import org.eclipse.jgit.diff.DiffEntry

@Serializable
data class OptimizePatch(
    val changedLineCount: ChangedLineCount,
    val changeType: PatchChangeType,
    val content: String,
    val path: String,
)

/**
 * from [org.eclipse.jgit.diff.DiffEntry.ChangeType]
 */
enum class PatchChangeType {
    ADD,
    MODIFY,
    DELETE,
    RENAME,
    COPY;

    companion object {
        fun from(changeType: DiffEntry.ChangeType): PatchChangeType {
            return when (changeType) {
                DiffEntry.ChangeType.ADD -> ADD
                DiffEntry.ChangeType.MODIFY -> MODIFY
                DiffEntry.ChangeType.DELETE -> DELETE
                DiffEntry.ChangeType.RENAME -> RENAME
                DiffEntry.ChangeType.COPY -> COPY
            }
        }
    }
}