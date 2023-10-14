package org.cc

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    var end: Pos,
    val start: Pos,
)

@Serializable
data class Pos(
    var column: Int,
    var line: Int,
    var offset: Int,
)