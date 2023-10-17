package cc.unitmesh.cf.code

import chapi.domain.core.CodeDataStruct
import chapi.parser.ParseMode
import kotlinx.serialization.Serializable
//import org.archguard.scanner.core.diffchanges.ChangedNode
import org.archguard.scanner.core.diffchanges.NodeRelation
import org.archguard.scanner.core.diffchanges.NodeRelationBuilder
import org.archguard.scanner.core.diffchanges.SHORT_ID_LENGTH
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.dircache.DirCacheIterator
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.FileTreeIterator
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.util.io.DisabledOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets


@Serializable
class DifferFile(
    val path: String,
    val dataStructs: List<CodeDataStruct>,
)

class ChangedEntry(
    val path: String,
    val file: String,
    val packageName: String,
    val className: String,
    val functionName: String = "",
    val code: String = "",
)

/**
 * The GitDiffer class is responsible for comparing and counting the number of changed calls between two revisions in a Git repository.
 * It extends the NodeRelationBuilder class.
 *
 * @param path The path to the Git repository.
 * @param branch The branch to compare.
 * @param loopDepth The depth of the loop for calculating reverse calls (default value is SHORT_ID_LENGTH).
 * @property baseLineDataTree A list of DifferFile objects representing the baseline AST tree.
 * @property differFileMap A mutable map of file paths to DifferFile objects.
 * @property changedFiles A mutable map of file paths to ChangedEntry objects representing changed files.
 * @property changedClasses A mutable map of file paths to ChangedEntry objects representing changed classes.
 * @property changedFunctions A mutable map of file paths to ChangedEntry objects representing changed functions.
 */
class GitDiffer(val path: String, private val branch: String = "master", private val loopDepth: Int = SHORT_ID_LENGTH) :
    NodeRelationBuilder() {
    private var baseLineDataTree: List<DifferFile> = listOf()
    private val differFileMap: MutableMap<String, DifferFile> = mutableMapOf()
    private val changedFiles: MutableMap<String, ChangedEntry> = mutableMapOf()
    private val changedClasses: MutableMap<String, ChangedEntry> = mutableMapOf()
    private val changedFunctions: MutableMap<String, ChangedEntry> = mutableMapOf()

    private val repository: Repository
    private val git: Git

    init {
        repository = FileRepositoryBuilder().findGitDir(File(path)).build()
        git = Git(repository).specifyBranch(branch)
    }

    /**
     * Counts the number of changed calls between two revisions.
     *
     * @param sinceRev The revision to start counting from.
     * @param untilRev The revision to stop counting at.
     * @return A list of ChangedNode objects representing the changed calls between the two revisions.
     */
    fun countBetween(sinceRev: String, untilRev: String): List<ChangedNode> {
        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)

        // 1. create based ast model from since revision commit
        this.baseLineDataTree = createBaselineAstTree(repository, since)

        // 2. calculate changed files to utils file
        for (commit in git.log().addRange(since, until).call()) {
            getChangedFiles(repository, commit)
        }

        // 3. count changed items reverse-call function
        this.baseLineDataTree.forEach { file -> this.fillFunctionMap(file.dataStructs) }
        this.baseLineDataTree.forEach { file -> this.fillReverseCallMap(file.dataStructs) }
        val nodes = this.calculateChange()

        // add path map to projects

        // 4. align to the latest file path (maybe), like: increment for path changes
        return nodes
    }

    /**
     * This method retrieves the difference (patch) between two given revisions in a Git repository.
     *
     * @param sinceRev The revision to start from.
     * @param untilRev The revision to end at.
     * @return A map containing the file paths as keys and the corresponding patch text as values.
     */
    fun patchBetween(sinceRev: String, untilRev: String): Map<String, String> {
        git.use {
            // 获取 sinceRev 和 untilRev 的 ObjectId
            val sinceObj: ObjectId = repository.resolve(sinceRev)
            val untilObj: ObjectId = repository.resolve(untilRev)

            // 获取两个提交之间的差异（补丁）
            val outputStream = ByteArrayOutputStream()
            val diffFormatter = DiffFormatter(outputStream)
            diffFormatter.setRepository(repository)
            diffFormatter.format(sinceObj, untilObj)

            // 将补丁转换为 Map
            val patchMap = mutableMapOf<String, String>()
            outputStream.toString().split("diff --git ").forEach {
                val lines = it.split(System.lineSeparator())
                if (lines.size <= 1) {
                    return@forEach
                }

                val split = lines[0].split(" b/")
                if (split.size > 1) {
                    val path = split[1]
                    val patch = it.substring(lines[0].length + 1)
                    patchMap[path] = Companion.trimDiff(patch)
                }
            }

            return patchMap
        }
    }


    private fun calculateChange(): List<ChangedNode> {
        return changedFunctions.map {
            val callName = it.value.packageName + "." + it.value.className + "." + it.value.functionName
            val nodeRelations: MutableList<NodeRelation> = mutableListOf()
            calculateReverseCalls(callName, nodeRelations, loopDepth) ?: listOf()

            ChangedNode(
                path = it.value.path,
                packageName = it.value.packageName,
                className = it.value.className,
                relations = nodeRelations,
                code = ""
            )
        }.toList()
    }

    private fun getChangedFiles(repository: Repository, revCommit: RevCommit) {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> compareWithBaseline(d, repository, revCommit) }
    }

    /**
     * Compares the given diff entry with the baseline data.
     *
     * @param diffEntry The diff entry to compare.
     * @param repository The repository containing the baseline data.
     * @param revCommit The commit containing the baseline data.
     */
    private fun compareWithBaseline(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit,
    ) {
        try {
            val treeWalk = TreeWalk.forPath(repository, diffEntry.newPath, revCommit.tree) ?: return

            val filePath = treeWalk.pathString

            val blobId = treeWalk.getObjectId(0)

            val newDataStructs: List<CodeDataStruct>

            val extension = File(filePath).extension
            newDataStructs = diffFileFromBlob(repository, blobId, filePath, extension)

            if (this.differFileMap[filePath] != null) {
                val oldDataStructs = this.differFileMap[filePath]!!.dataStructs

                // compare for sized
                if (newDataStructs.size != oldDataStructs.size) {
                    val difference = newDataStructs.filterNot { oldDataStructs.contains(it) }
                    difference.forEach {
                        this.changedFiles[filePath] =
                            ChangedEntry(filePath, filePath, it.Package, it.NodeName, it.Content)
                    }
                } else {
                    // compare for field
                    newDataStructs.forEachIndexed { index, ds ->
                        // in first version, if field changed, just make data structure change will be simple
                        if (ds.Fields.size != oldDataStructs[index].Fields.size) {
                            this.changedClasses[filePath] =
                                ChangedEntry(filePath, filePath, ds.Package, ds.NodeName, ds.Content)
                        } else if (ds.Fields != oldDataStructs[index].Fields) {
                            this.changedClasses[filePath] =
                                ChangedEntry(filePath, filePath, ds.Package, ds.NodeName, ds.Content)
                        }

                        // compare for function sizes
                        if (ds.Functions != oldDataStructs[index].Functions) {
                            val difference = ds.Functions.filterNot { oldDataStructs[index].Functions.contains(it) }
                            difference.forEach {
                                this.changedFunctions[filePath] =
                                    ChangedEntry(filePath, filePath, ds.Package, ds.NodeName, it.Name, it.Content)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun DiffFormatter.config(repository: Repository): DiffFormatter {
        setRepository(repository)
        setDiffComparator(RawTextComparator.DEFAULT)
        isDetectRenames = true
        return this
    }

    private fun getParent(revCommit: RevCommit): RevCommit? {
        return if (revCommit.parentCount == 0) {
            null
        } else {
            revCommit.getParent(0)
        }
    }

    private fun createBaselineAstTree(repository: Repository, since: ObjectId): List<DifferFile> {
        val rw = RevWalk(repository)
        val tw = TreeWalk(repository)

        val commitToCheck: RevCommit = rw.parseCommit(since)
        tw.addTree(commitToCheck.tree)
        tw.addTree(DirCacheIterator(repository.readDirCache()))
        tw.addTree(FileTreeIterator(repository))

        tw.isRecursive = true

        val files: MutableList<DifferFile> = mutableListOf()
        while (tw.next()) {
            try {
                val pathString = tw.pathString
                val blobId: ObjectId = tw.getObjectId(0)

                val extension = File(pathString).extension

                val dataStructs = diffFileFromBlob(repository, blobId, pathString, extension)
                val differFile = DifferFile(path = pathString, dataStructs = dataStructs)

                differFileMap[pathString] = differFile
                files += differFile
            } catch (e: Exception) {
                // ignore
            }
        }

        return files
    }

    // TODO get the data structure from the file or cache while already got from language analyser
    private fun diffFileFromBlob(
        repository: Repository,
        blobId: ObjectId,
        pathString: String,
        extension: String,
    ): List<CodeDataStruct> {
        val fileName = File(pathString).name
        val content = repository.newObjectReader().use { objectReader ->
            String(objectReader.open(blobId).bytes, StandardCharsets.UTF_8)
        }
        return when (extension) {
            "kt", "kts" -> {
                val analyser = chapi.ast.kotlinast.KotlinAnalyser()
                analyser.analysis(content, fileName, ParseMode.Full).run {
                    DataStructures.map { it.apply { it.FilePath = pathString } }
                }
            }

            "java" -> {
                val analyser = chapi.ast.javaast.JavaAnalyser()
                val basicNodes = analyser.identBasicInfo(content, fileName).run {
                    DataStructures.map { ds -> ds.apply { ds.Imports = Imports } }
                }
                val classes = basicNodes.map { it.getClassFullName() }
                analyser.identFullInfo(content, fileName, classes, basicNodes).run {
                    DataStructures.map { ds -> ds.apply { ds.Imports = Imports } }
                }
            }

            else -> emptyList()
        }
    }

    private fun Git.specifyBranch(branch: String): Git = apply {
        checkout().setName(branch).call()
    }

    fun gitRepositoryUrl(): String {
        val repoUrl = git.repository.config.getString("remote", "origin", "url")
        return repoUrl
    }

    fun commitMessagesBetween(sinceCommit: String, untilCommit: String): Map<String, String> {
        val since: ObjectId = git.repository.resolve(sinceCommit)
        val until: ObjectId = git.repository.resolve(untilCommit)

        val commits = git.log().addRange(since, until).call()
        return commits.associate { it.name to it.shortMessage }
    }

    companion object {
        private val revisionRegex = Regex("\\(revision [^)]+\\)")

        fun trimDiff(diffString: String): String {
            val lines = diffString.lines()
            val destination = ArrayList<String>()
            for (line in lines) {
                if (line.startsWith("diff --git ") || line.startsWith("index ") || line.startsWith("Index ")) continue

                if (line == "===================================================================") continue

                if (line.startsWith("---") || line.startsWith("+++")) {
                    val result = revisionRegex.replace(line, "")
                    destination.add(result)
                } else {
                    destination.add(line)
                }
            }
            return destination.joinToString(System.lineSeparator())
        }
    }
}
