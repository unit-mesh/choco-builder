package cc.unitmesh.rag

import cc.unitmesh.cf.code.CodeSplitter

class CodeDsl(file: String) {
    val codeSplitter = CodeSplitter(file)

    fun split(): List<String> {
        return listOf()
    }
}
