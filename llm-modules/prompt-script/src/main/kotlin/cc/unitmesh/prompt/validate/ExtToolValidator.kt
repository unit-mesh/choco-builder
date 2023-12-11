package cc.unitmesh.prompt.validate

/**
 * Extension Tool is validate command, like `plantuml xxx.puml`
 */
class ExtToolValidator(private val execCommand: String, override val llmResult: String, val options: Map<String, String>) :
    Validator {
    override fun validate(): Boolean {
        // a exec command like `ls -l`
        val commandList = execCommand.split(" ").toMutableList()

        for ((key, value) in options) {
            commandList.add(key)
            commandList.add(value)
        }

        if (llmResult.isNotEmpty()) {
            commandList.add(llmResult)
        }

        val processBuilder = ProcessBuilder(commandList)

        return try {
            val process = processBuilder.start()
            val exitCode = process.waitFor()
            // show stdout
            process.inputStream.bufferedReader().use { println(it.readText()) }
            // show stderr
            process.errorStream.bufferedReader().use { println(it.readText()) }
            exitCode == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
