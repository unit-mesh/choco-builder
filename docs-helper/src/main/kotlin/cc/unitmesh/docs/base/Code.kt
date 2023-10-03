/**
 * Copyright 2019 Pinterest, Inc.
 * Copyright 2016-2019 Stanley Shyiko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package cc.unitmesh.docs.base

import org.jetbrains.kotlin.konan.file.file
import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString

public const val STDIN_FILE: String = "<stdin>"

public class Code private constructor(
    public val content: String,
    public val fileName: String?,
    public val filePath: Path?,
    public val script: Boolean,
    public val isStdIn: Boolean,
) {
    public fun fileNameOrStdin(): String =
        if (isStdIn) {
            STDIN_FILE
        } else {
            fileName.orEmpty()
        }

    public fun filePathOrStdin(): String =
        if (isStdIn) {
            STDIN_FILE
        } else {
            filePath?.pathString.orEmpty()
        }

    public companion object {
        /**
         * Create [Code] from a [file] containing valid Kotlin code or script. The '.editorconfig' files on the path to [file] are taken
         * into account.
         */
        public fun fromFile(file: File): Code =
            Code(
                content = file.readText(),
                fileName = file.name,
                filePath = file.toPath(),
                script = file.name.endsWith(".kts", ignoreCase = true),
                isStdIn = false,
            )

        public fun fromPath(path: Path): Code {
            // Resolve the file based on the file system of the original path given.
            val file =
                path
                    .fileSystem
                    .file(path.pathString)
            return Code(
                content = file.readStrings().joinToString(separator = "\n"),
                fileName = file.name,
                filePath = path,
                script = file.name.endsWith(".kts", ignoreCase = true),
                isStdIn = false,
            )
        }

        public fun fromSnippet(
            content: String,
            script: Boolean = false,
        ): Code =
            Code(
                content = content,
                filePath = null,
                fileName = null,
                script = script,
                isStdIn = true,
            )

        /**
         * Create [Code] by reading the snippet from 'stdin'. No '.editorconfig' are taken into account.  The '.editorconfig' files on the
         * filesystem are ignored as the snippet is not associated with a file path. Use [Code.fromFile] for scanning a file while at the
         * same time respecting the '.editorconfig' files on the path to the file.
         */
        public fun fromStdin(): Code = fromSnippet(String(System.`in`.readBytes()))
    }
}

