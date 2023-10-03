package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator
import java.nio.file.Path

class PromptScriptDocGen(val rootDir: Path) : DocGenerator() {
    private val processor: FileProcessor = FileProcessor()

    override fun execute() {
        val lists = processor.process(rootDir)
        println(lists)
    }
}
