package cc.unitmesh.genius.helper

import java.io.File

abstract class FileGenerator {
    protected fun rootDir(): File {
        return File("").absoluteFile
    }

    abstract fun createFile()
}