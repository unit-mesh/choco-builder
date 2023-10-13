package cc.unitmesh.cf.code.command

import org.slf4j.Logger

interface ExecListeners {
    fun stdout(data: String) {}
    fun stderr(data: String) {}
}

class LoggerExecListeners(private val logger: Logger) : ExecListeners {
    override fun stdout(data: String) {
        logger.info(data)
    }

    override fun stderr(data: String) {
        logger.error(data)
    }
}

class StringListExecListeners(private var stdout: MutableList<String>, private var stderr: MutableList<String>) : ExecListeners {
    override fun stdout(data: String) {
        stdout += data
    }

    override fun stderr(data: String) {
        stderr += data
    }
}