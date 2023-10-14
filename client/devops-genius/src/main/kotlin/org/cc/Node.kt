package org.cc

data class Node(
    val type: String,
    var value: String = "",
    val position: Position,
    var children: List<Node> = listOf(),
) {

}
