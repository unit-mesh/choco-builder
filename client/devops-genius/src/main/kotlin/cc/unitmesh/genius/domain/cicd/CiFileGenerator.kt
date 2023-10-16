package cc.unitmesh.genius.domain.cicd

import java.io.File

abstract class CiFileGenerator {
    protected fun rootDir(): File {
        return File("").absoluteFile
    }

    abstract fun createFile()
}