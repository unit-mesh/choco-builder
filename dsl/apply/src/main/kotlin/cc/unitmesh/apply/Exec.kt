package cc.unitmesh.apply

import java.io.File

class Exec(filename: String, function: () -> Unit) {
    constructor(filename: String) : this(
        filename,
        function = {}
    )

    private val output = File(filename)

    init {
        function()
    }

    fun run(): File {
        return output
    }
}
