package cc.unitmesh.apply

import java.io.File

class Exec {
    fun runJar(fileName: File): File {
        val process = ProcessBuilder("java", "-jar", fileName.absolutePath)
            .inheritIO()
            .start()

        process.waitFor()
        return File("temp")
    }

}
