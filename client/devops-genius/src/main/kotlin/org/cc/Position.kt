package org.cc

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val start: Pos,
    var end: Pos,
)

@Serializable
data class Pos(
    var line: Int,
    var column: Int,
    var offset: Int,
)