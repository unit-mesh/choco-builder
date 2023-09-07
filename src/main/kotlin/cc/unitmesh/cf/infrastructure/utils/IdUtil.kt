package cc.unitmesh.cf.infrastructure.utils

import java.util.*

object IdUtil {
    fun uuid(): String {
        return UUID.randomUUID().toString()
    }
}