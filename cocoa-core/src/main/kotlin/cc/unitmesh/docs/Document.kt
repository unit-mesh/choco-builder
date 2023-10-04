package cc.unitmesh.docs

annotation class Document()

annotation class SampleCode(
    val name: String = "",
    val content: String = "",
    val startBlock: String = "// start-sample",
    val endBlock: String = "// end-sample",
)
