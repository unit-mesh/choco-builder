package cc.unitmesh.cf.code

import kotlinx.serialization.Serializable
import org.archguard.scanner.core.diffchanges.NodeRelation

@Serializable
class ChangedNode(
    val path: String,
    val packageName: String,
    val className: String,
    val relations: List<NodeRelation>,
    val code: String,
) {

}
