package cc.unitmesh.cf.infrastructure.utils

import java.util.*

fun nextId(): String {
    return UUID.randomUUID().toString()
}