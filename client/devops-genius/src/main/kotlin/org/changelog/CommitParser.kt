package org.changelog

import java.util.regex.Pattern

fun createCommitObject(): Commit {
    return Commit()
}

/**
 * Append a newline to a string.
 * @param src
 * @param line
 * @returns String with appended newline.
 */
fun appendLine(src: String?, line: String?): String {
    return if (src != null) {
        "$src\n${line ?: ""}"
    } else {
        line ?: ""
    }
}


/**
 * Remove leading and trailing newlines.
 * @param input
 * @returns String without leading and trailing newlines.
 */
fun trimNewLines(input: String): String {
    val matches = input.indexOfAny(charArrayOf('\r', '\n'))

    if (matches == -1) {
        return ""
    }

    val firstIndex = matches
    var lastIndex = input.length - 1

    while (input[lastIndex] == '\r' || input[lastIndex] == '\n') {
        lastIndex--
    }

    return input.substring(firstIndex, lastIndex + 1)
}

/**
 * Creates a function that filters out comments lines.
 * @param char
 * @returns Comment filter function.
 */
fun getCommentFilter(char: String?): (String) -> Boolean {
    return if (char != null) {
        { line: String -> !line.startsWith(char) }
    } else {
        { true }
    }
}

val SCISSOR = "# ------------------------ >8 ------------------------"

/**
 * Select lines before the scissor.
 * @param lines
 * @returns Lines before the scissor.
 */
fun truncateToScissor(lines: List<String>): List<String> {
    val scissorIndex = lines.indexOf(SCISSOR)

    if (scissorIndex == -1) {
        return lines
    }

    return lines.subList(0, scissorIndex)
}

/**
 * Filter out GPG sign lines.
 * @param line
 * @returns True if the line is not a GPG sign line.
 */
fun gpgFilter(line: String): Boolean {
    return !line.matches(Regex("^\\s*gpg:"))
}

class CommitParser(
    val options: ParserOptions = ParserOptions.defaultOptions()
) {
    private val regexes: ParserRegexes = RegexParser.getParserRegexes(options)
    private val lines = mutableListOf<String>()
    private var lineIndex = 0
    private var commit = createCommitObject()

    private fun currentLine() = lines.getOrNull(lineIndex)

    private fun nextLine() = lines.getOrNull(lineIndex++)

    private fun isLineAvailable() = lineIndex < lines.size

    private fun parseReference(input: String, action: String?): CommitReference? {
        val regexes = this.regexes
        val matches = regexes.referenceParts.find(input)

        if (matches == null || matches.groups.isEmpty()) {
            return null
        }

        val raw = matches.groups[0]?.value
        var repository: String? = matches.groups[1]?.value
        val prefix = matches.groups[2]?.value ?: ""
        val issue = matches.groups[3]?.value ?: ""
        var owner: String? = null

        if (!repository.isNullOrEmpty()) {
            val slashIndex = repository.indexOf('/')
            if (slashIndex != -1) {
                owner = repository.substring(0, slashIndex)
                repository = repository.substring(slashIndex + 1)
            }
        }


        return CommitReference(raw!!, action, owner, repository, prefix, issue)
    }

    fun parseReferences(input: String): List<CommitReference> {
        val regexes = this.regexes
        val regex = if (regexes.references.find(input) != null) {
            regexes.references
        } else {
            Regex("()(.+)", setOf(RegexOption.IGNORE_CASE))
        }
        val references = mutableListOf<CommitReference>()
        var matches: MatchResult?
        var action: String?
        var sentence: String
        var reference: CommitReference?
        var shouldBreak: Boolean = false

        while (true) {
            if (shouldBreak) {
                break
            }

            matches = regex.find(input)

            if (matches == null) {
                break
            }

            action = matches.groupValues.getOrNull(1)
            sentence = matches.groupValues.getOrNull(2) ?: ""

            while (true) {
                reference = this.parseReference(sentence, action)
                println("parseReferences: $reference")

                if (reference == null) {
                    shouldBreak = true
                    break
                }

                references.add(reference)
            }

        }

        return references
    }

    private fun skipEmptyLines() {
        var line = currentLine()

        while (line != null && line.isBlank()) {
            nextLine()
            line = currentLine()
        }
    }

    //  private parseMerge() {
    //    const { commit, options } = this
    //    const correspondence = options.mergeCorrespondence || []
    //    const merge = this.currentLine()
    //    const matches = merge && options.mergePattern
    //      ? merge.match(options.mergePattern)
    //      : null
    //
    //    if (matches) {
    //      this.nextLine()
    //
    //      commit.merge = matches[0] || null
    //
    //      correspondence.forEach((key, index) => {
    //        commit[key] = matches[index + 1] || null
    //      })
    //
    //      return true
    //    }
    //
    //    return false
    //  }
    fun parseMerge(): Boolean {
        val commit = this.commit
        val options = this.options
        val correspondence = options.mergeCorrespondence ?: emptyList()
        val merge = this.currentLine()
        val matches = merge?.let { options.mergePattern?.find(it) }

        if (matches != null) {
            this.nextLine()

            commit.merge = matches.groupValues.getOrNull(0)

            correspondence.forEachIndexed { index, key ->
                commit.meta[key] = matches.groupValues.getOrNull(index + 1)
            }

            return true
        }

        return false
    }

    //  private parseHeader(isMergeCommit: boolean) {
    //    if (isMergeCommit) {
    //      this.skipEmptyLines()
    //    }
    //
    //    const { commit, options } = this
    //    const correspondence = options.headerCorrespondence || []
    //    const header = this.nextLine()
    //    const matches = header && options.headerPattern
    //      ? header.match(options.headerPattern)
    //      : null
    //
    //    if (header) {
    //      commit.header = header
    //    }
    //
    //    if (matches) {
    //      correspondence.forEach((key, index) => {
    //        commit[key] = matches[index + 1] || null
    //      })
    //    }
    //  }
    fun parseHeader(isMergeCommit: Boolean) {
        if (isMergeCommit) {
            this.skipEmptyLines()
        }

        val commit = this.commit
        val options = this.options
        val correspondence = options.headerCorrespondence ?: emptyList()
        val header = this.nextLine()
        val matches = header?.let { options.headerPattern?.find(it) }

        if (header != null) {
            commit.header = header
        }

        if (matches != null) {
            correspondence.forEachIndexed { index, key ->
                commit.meta[key] = matches.groupValues.getOrNull(index + 1)
            }
        }
    }

    //private parseMeta() {
    //    const {
    //      options,
    //      commit
    //    } = this
    //
    //    if (!options.fieldPattern || !this.isLineAvailable()) {
    //      return false
    //    }
    //
    //    let matches: RegExpMatchArray | null
    //    let field: string | null = null
    //    let parsed = false
    //
    //    while (this.isLineAvailable()) {
    //      matches = this.currentLine().match(options.fieldPattern)
    //
    //      if (matches) {
    //        field = matches[1] || null
    //        this.nextLine()
    //        continue
    //      }
    //
    //      if (field) {
    //        parsed = true
    //        commit[field] = appendLine(commit[field], this.currentLine())
    //        this.nextLine()
    //      } else {
    //        break
    //      }
    //    }
    //
    //    return parsed
    //  }
    fun parseMeta(): Boolean {
        val options = this.options
        val commit = this.commit

        if (options.fieldPattern == null || !this.isLineAvailable()) {
            return false
        }

        var matches: MatchResult?
        var field: String? = null
        var parsed = false

        while (this.isLineAvailable()) {
            matches = options.fieldPattern?.find(this.currentLine()!!)

            if (matches != null) {
                field = matches.groupValues.getOrNull(1)
                this.nextLine()
                continue
            }

            if (field != null) {
                parsed = true
                commit.meta[field] = appendLine(commit.meta[field], this.currentLine()!!)
                this.nextLine()
            } else {
                break
            }
        }

        return parsed
    }

    //  private parseNotes() {
    //    const {
    //      regexes,
    //      commit
    //    } = this
    //
    //    if (!this.isLineAvailable()) {
    //      return false
    //    }
    //
    //    const matches = this.currentLine().match(regexes.notes)
    //    let references: CommitReference[] = []
    //
    //    if (matches) {
    //      const note: CommitNote = {
    //        title: matches[1],
    //        text: matches[2]
    //      }
    //
    //      commit.notes.push(note)
    //      commit.footer = appendLine(commit.footer, this.currentLine())
    //      this.nextLine()
    //
    //      while (this.isLineAvailable()) {
    //        if (this.parseMeta()) {
    //          return true
    //        }
    //
    //        if (this.parseNotes()) {
    //          return true
    //        }
    //
    //        references = this.parseReferences(this.currentLine())
    //
    //        if (references.length) {
    //          commit.references.push(...references)
    //        } else {
    //          note.text = appendLine(note.text, this.currentLine())
    //        }
    //
    //        commit.footer = appendLine(commit.footer, this.currentLine())
    //        this.nextLine()
    //
    //        if (references.length) {
    //          break
    //        }
    //      }
    //
    //      return true
    //    }
    //
    //    return false
    //  }
    fun parseNotes(): Boolean {
        val regexes = this.regexes
        val commit = this.commit

        if (!this.isLineAvailable()) {
            return false
        }

        val matches = regexes.notes.find(this.currentLine()!!)
        var references: List<CommitReference> = emptyList()

        if (matches != null) {
            val note = CommitNote(
                title = matches.groupValues.getOrNull(1) ?: "",
                text = matches.groupValues.getOrNull(2) ?: "",
            )

            commit.notes.toMutableList().add(note)
            commit.footer = appendLine(commit.footer, this.currentLine())
            this.nextLine()

            while (this.isLineAvailable()) {
                if (this.parseMeta()) {
                    return true
                }

                if (this.parseNotes()) {
                    return true
                }

                references = this.parseReferences(this.currentLine()!!)

                if (references.isNotEmpty()) {
                    commit.references.toMutableList().addAll(references)
                } else {
                    note.text = appendLine(note.text, this.currentLine())
                }

                commit.footer = appendLine(commit.footer, this.currentLine())
                this.nextLine()

                if (references.isNotEmpty()) {
                    break
                }
            }

            return true
        }

        return false
    }

    //private parseBodyAndFooter(isBody: boolean) {
    //    const { commit } = this
    //
    //    if (!this.isLineAvailable()) {
    //      return isBody
    //    }
    //
    //    const references = this.parseReferences(this.currentLine())
    //    const isStillBody = !references.length && isBody
    //
    //    if (isStillBody) {
    //      commit.body = appendLine(commit.body, this.currentLine())
    //    } else {
    //      commit.references.push(...references)
    //      commit.footer = appendLine(commit.footer, this.currentLine())
    //    }
    //
    //    this.nextLine()
    //
    //    return isStillBody
    //  }
    //
    //  private parseBreakingHeader() {
    //    const {
    //      commit,
    //      options
    //    } = this
    //
    //    if (!options.breakingHeaderPattern || commit.notes.length || !commit.header) {
    //      return
    //    }
    //
    //    const matches = commit.header.match(options.breakingHeaderPattern)
    //
    //    if (matches) {
    //      commit.notes.push({
    //        title: 'BREAKING CHANGE',
    //        text: matches[3]
    //      })
    //    }
    //  }
    fun parseBodyAndFooter(isBody: Boolean): Boolean {
        val commit = this.commit

        if (!this.isLineAvailable()) {
            return isBody
        }

        val references = this.parseReferences(this.currentLine()!!)
        val isStillBody = references.isEmpty() && isBody

        if (isStillBody) {
            commit.body = appendLine(commit.body, this.currentLine())
        } else {
            commit.references.toMutableList().addAll(references)
            commit.footer = appendLine(commit.footer, this.currentLine())
        }

        this.nextLine()

        return isStillBody
    }

    //  private parseBreakingHeader() {
    //    const {
    //      commit,
    //      options
    //    } = this
    //
    //    if (!options.breakingHeaderPattern || commit.notes.length || !commit.header) {
    //      return
    //    }
    //
    //    const matches = commit.header.match(options.breakingHeaderPattern)
    //
    //    if (matches) {
    //      commit.notes.push({
    //        title: 'BREAKING CHANGE',
    //        text: matches[3]
    //      })
    //    }
    fun parseBreakingHeader() {
        val commit = this.commit
        val options = this.options

        if (options.breakingHeaderPattern == null || commit.notes.isNotEmpty() || commit.header == null) {
            return
        }

        val matches = options.breakingHeaderPattern.find(commit.header!!)

        if (matches != null) {
            commit.notes.toMutableList().add(
                CommitNote(
                    title = "BREAKING CHANGE",
                    text = matches.groupValues.getOrNull(3) ?: "",
                )
            )
        }
    }

    //private parseMentions(input: string) {
    //    const {
    //      commit,
    //      regexes
    //    } = this
    //    let matches: RegExpExecArray | null
    //
    //    for (;;) {
    //      matches = regexes.mentions.exec(input)
    //
    //      if (!matches) {
    //        break
    //      }
    //
    //      commit.mentions.push(matches[1])
    //    }
    //  }
    fun parseMentions(input: String) {
        val commit = this.commit
        val regexes = this.regexes
        var matches: MatchResult?

        while (true) {
            matches = regexes.mentions.find(input)

            if (matches == null) {
                break
            }

            commit.mentions.toMutableList().add(matches.groupValues.getOrNull(1) ?: "")
        }
    }

    // private parseRevert(input: string) {
    //    const {
    //      commit,
    //      options
    //    } = this
    //    const correspondence = options.revertCorrespondence || []
    //    const matches = options.revertPattern
    //      ? input.match(options.revertPattern)
    //      : null
    //
    //    if (matches) {
    //      commit.revert = correspondence.reduce<CommitMeta>((meta, key, index) => {
    //        meta[key] = matches[index + 1] || null
    //
    //        return meta
    //      }, {})
    //    }
    //  }
    fun parseRevert(input: String) {
        val commit = this.commit
        val options = this.options
        val correspondence = options.revertCorrespondence ?: emptyList()
        val matches = options.revertPattern?.find(input)

        if (matches != null) {
            commit.revert = correspondence.mapIndexed { index, key ->
                key to matches.groupValues.getOrNull(index + 1)
            }.toMap()
        }
    }

    //  private cleanupCommit() {
    //    const { commit } = this
    //
    //    if (commit.body) {
    //      commit.body = trimNewLines(commit.body)
    //    }
    //
    //    if (commit.footer) {
    //      commit.footer = trimNewLines(commit.footer)
    //    }
    //
    //    commit.notes.forEach((note) => {
    //      note.text = trimNewLines(note.text)
    //    })
    //  }
    fun cleanupCommit() {
        val commit = this.commit

        if (commit.body != null) {
            commit.body = trimNewLines(commit.body!!)
        }

        if (commit.footer != null) {
            commit.footer = trimNewLines(commit.footer!!)
        }

        commit.notes.forEach { note ->
            note.text = trimNewLines(note.text)
        }
    }

    /**
     * Parse commit message string into an object.
     * @param input - Commit message string.
     * @returns Commit object.
     */
    fun parse(input: String): Commit {
        if (input.trim().isEmpty()) {
            throw IllegalArgumentException("Expected a raw commit")
        }

        val commentFilter = getCommentFilter(this.options.commentChar)
        val rawLines = trimNewLines(input).split(Regex("\\r?\\n"))
        val lines = truncateToScissor(rawLines).filter { line -> commentFilter(line) && gpgFilter(line) }

        this.lines.clear()
        this.lines.addAll(lines)
        this.lineIndex = 0
        val commit = createCommitObject()

        this.commit = commit

        val isMergeCommit = this.parseMerge()

        this.parseHeader(isMergeCommit)

        if (commit.header != null) {
            commit.references.toMutableList().addAll(this.parseReferences(commit.header!!))
        }

        var isBody = true

        while (this.isLineAvailable()) {
            this.parseMeta()

            if (this.parseNotes()) {
                isBody = false
            }

            if (!this.parseBodyAndFooter(isBody)) {
                isBody = false
            }
        }

        println("parseBreakingHeader")
        this.parseBreakingHeader()
        println("parseMentions")
        this.parseMentions(input)
        println("parseRevert")
        this.parseRevert(input)
        println("cleanupCommit")
        this.cleanupCommit()

        return commit
    }
}