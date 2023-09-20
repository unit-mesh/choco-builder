package cc.unitmesh.apply

import java.io.File

class Exec(val filename: String, function: () -> Unit) {
    constructor(filename: String) : this(
        filename,
        function = {}
    )

    init {
        function()
    }

    fun run(): File? {
        return null
    }
}
