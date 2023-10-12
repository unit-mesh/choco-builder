package cc.unitmesh.prompt.validate

/**
 * Extension Tool is validate command, like `plantuml xxx.puml`
 */
class ExtToolValidator(private val execCommand: String, override val input: String, val options: Map<String, String>) :
    Validator {
    override fun validate(): Boolean {
        val commandList = mutableListOf<String>()
        commandList.add(execCommand)

        for ((key, value) in options) {
            commandList.add("--$key")
            commandList.add(value)
        }

        commandList.add(input)

        val processBuilder = ProcessBuilder(commandList)

        return try {
            val process = processBuilder.start()
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
