package cc.unitmesh.cf.core.utils

import java.util.*

object IdUtil {
    fun uuid(): String {
        return UUID.randomUUID().toString()
    }
}