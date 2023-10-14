package org.cc

data class Position(
    var end: Pos,
    val start: Pos,
)

data class Pos(
    var column: Int,
    var line: Int,
    var offset: Int,
)