package cc.unitmesh.prompt.validate

/**
 * Extension Tool is validate command, like `plantuml xxx.puml`
 */
class ExtToolValidator(private val execCommand: String) : Validator {
    override fun validate(): Boolean {
        val process = Runtime.getRuntime().exec(execCommand)
        val exitCode = process.waitFor()
        return exitCode == 0
    }
}
