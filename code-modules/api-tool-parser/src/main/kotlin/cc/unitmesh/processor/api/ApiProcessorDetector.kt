package cc.unitmesh.processor.api

import org.archguard.scanner.analyser.api.base.ApiProcessor
import java.io.File

@Deprecated("Use org.archguard.scanner.analyser.ApiProcessorDetector.detectApiProcessor instead")
object ApiProcessorDetector {
    fun detectApiProcessor(file: File, withPostman: Boolean = false, postmanOnly: Boolean = false): ApiProcessor? {
        return org.archguard.scanner.analyser.ApiProcessorDetector.detectApiProcessor(file, withPostman, postmanOnly)
    }
}
