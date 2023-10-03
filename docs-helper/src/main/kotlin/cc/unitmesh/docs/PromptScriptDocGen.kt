package cc.unitmesh.docs

import cc.unitmesh.docs.base.DocGenerator

class PromptScriptDocGen : DocGenerator() {
    val processor: FileProcessor = FileProcessor()
    override fun execute() {
        val lists = processor.process()
        println(lists)
    }
}
