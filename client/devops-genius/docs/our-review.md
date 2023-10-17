You are a senior software developer, who can help me do code review a commit.

1. Please provide an overview of the business objectives and the context behind this commit. This will ensure that the code aligns with the project's requirements and goals.
1. If possible, focus on critical algorithms, logical flow, and design decisions within the code. Discuss how these changes impact the core functionality and the overall structure of the code.
1. Try to identify and highlight any potential issues or risks introduced by these code changes. This will help reviewers pay special attention to areas that may require improvement or further analysis.
1. Emphasize the importance of compatibility and consistency with the existing codebase. Ensure that the code adheres to the established standards and practices for code uniformity and long-term maintainability.
1. Lastly, provide a concise high-level summary that encapsulates the key aspects of this commit. This summary should enable reviewers to quickly grasp the major changes in this update.

PS: Your insights and feedback are invaluable in ensuring the quality and reliability of this code. Thank you for your assistance.

Business Context: ## Prompt 上下文

- 业务逻辑。
    - 文档信息。最新业务相关文档
- 代码逻辑。
    - 代码是否符合编码规范。
    - 代码是否符合设计规范。
- 需求信息。标准的提交格式：
    - feat(devops): init first review command #8

## Prompt 策略

1. 如果变更的代码行数少，则只审核业务含义 —— 根据提交信息，解析对应的 story 名称，然后进行检查。
2. 根据变更的代码，生成对应的代码信息，作为上下文的一部分。
3. 如果变更的行数多，则需要进行代码逻辑的检查，以及对应的语法检查。
4. 如果单次变更的行数过多，则需要进行拆分。

Commit Message: feat(differ): add for handle patches match #8

Code Changes: --- /dev/null
+++ b/code-modules/git-differ/src/main/kotlin/cc/unitmesh/cf/code/ChangedLineCount.kt
@@ -0,0 +1,9 @@
+package cc.unitmesh.cf.code
+
+import kotlinx.serialization.Serializable
+
+@Serializable
+data class ChangedLineCount(
+    val added: Int,
+    val deleted: Int,
     +)

index 0ea0eb4..efb0fda 100644
--- a/code-modules/git-differ/src/main/kotlin/cc/unitmesh/cf/code/GitDiffer.kt
+++ b/code-modules/git-differ/src/main/kotlin/cc/unitmesh/cf/code/GitDiffer.kt
@@ -39,7 +39,7 @@
val functionName: String = "",
val code: String = "",
val addedLines: Int = 0,
-    val deletedLines: Int = 0
+    val deletedLines: Int = 0,
     )

/**
@@ -108,73 +108,87 @@
* @param untilRev The revision to end at.
* @return A map containing the file paths as keys and the corresponding patch text as values.
*/
-    fun patchBetween(sinceRev: String, untilRev: String): Map<String, String> {
-        git.use {
-            // 获取 sinceRev 和 untilRev 的 ObjectId
-            val sinceObj: ObjectId = repository.resolve(sinceRev)
-            val untilObj: ObjectId = repository.resolve(untilRev)
+    fun patchBetween(sinceRev: String, untilRev: String): Map<String, OptimizePatch> {
+        // 获取 sinceRev 和 untilRev 的 ObjectId
+        val sinceObj: ObjectId = repository.resolve(sinceRev)
+        val untilObj: ObjectId = repository.resolve(untilRev)

-            // 获取两个提交之间的差异（补丁）
-            val outputStream = ByteArrayOutputStream()
-            val diffFormatter = DiffFormatter(outputStream)
-            diffFormatter.setRepository(repository)
-            diffFormatter.format(sinceObj, untilObj)
+        // 获取两个提交之间的差异（补丁）
+        val outputStream = ByteArrayOutputStream()
+        val diffFormatter = DiffFormatter(outputStream)
+        diffFormatter.setRepository(repository)
+        diffFormatter.format(sinceObj, untilObj)

-            summaryFileDiff(diffFormatter, sinceObj, untilObj)
+        val diffs: List<DiffEntry> = diffFormatter.scan(sinceObj, untilObj)
+        val patchMap = mutableMapOf<String, OptimizePatch>()

-            // 将补丁转换为 Map
-            val patchMap = mutableMapOf<String, String>()
-            outputStream.toString().split("
new file mode 100644
index 0000000..d29effd
--- /dev/null
+++ b/code-modules/git-differ/src/main/kotlin/cc/unitmesh/cf/code/OptimizePatch.kt
@@ -0,0 +1,35 @@
+package cc.unitmesh.cf.code
+
+import kotlinx.serialization.Serializable
+import org.eclipse.jgit.diff.DiffEntry
+
+@Serializable
+data class OptimizePatch(
+    val changedLineCount: ChangedLineCount,
+    val changeType: PatchChangeType,
+    val content: String,
+    val path: String,
     +)
+
+/**
+ * from [org.eclipse.jgit.diff.DiffEntry.ChangeType]
+ */
  +enum class PatchChangeType {
+    ADD,
+    MODIFY,
+    DELETE,
+    RENAME,
+    COPY;
+
+    companion object {
+        fun from(changeType: DiffEntry.ChangeType): PatchChangeType {
+            return when (changeType) {
+                DiffEntry.ChangeType.ADD -> ADD
+                DiffEntry.ChangeType.MODIFY -> MODIFY
+                DiffEntry.ChangeType.DELETE -> DELETE
+                DiffEntry.ChangeType.RENAME -> RENAME
+                DiffEntry.ChangeType.COPY -> COPY
+            }
+        }
+    }
     +}

现在，作为你的技术负责人，我只关注一些重点的 code review 事项，请给我一个关键性的总结。
Take deep breath, Submit your summary under 5 sentences in here:
