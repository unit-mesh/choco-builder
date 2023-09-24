package cc.unitmesh.rag

import java.io.File
import java.io.IOException

object Exec {
    fun runJar(fileName: File, args: List<String>): File {
        try {
            val processBuilder = ProcessBuilder("java", "-jar", fileName.absolutePath)

            // Add any command-line arguments to the process
            processBuilder.command().addAll(args)

            // Start the process
            val process = processBuilder.start()

            // Wait for the process to complete (optional)
            val exitCode = process.waitFor()

            // Check if the process terminated successfully
            if (exitCode == 0) {
                println("Process completed successfully")
            } else {
                println("Process failed with exit code $exitCode")
            }

            // You can add additional code to handle the process output if needed
            return fileName // Return the input File as a result
        } catch (e: IOException) {
            // Handle any exceptions that may occur
            e.printStackTrace()
            throw e
        }
    }
}
