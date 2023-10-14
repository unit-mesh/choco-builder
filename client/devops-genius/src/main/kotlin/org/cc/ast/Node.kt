package org.cc.ast

import kotlinx.serialization.Serializable

@Serializable
data class Node(
    val type: String,
    var value: String = "",
    val position: Position,
    var children: List<Node> = listOf(),
) {

}
