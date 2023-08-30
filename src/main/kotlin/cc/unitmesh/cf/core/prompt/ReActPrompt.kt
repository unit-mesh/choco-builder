package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class ReActPrompt(
    @SerialName("标题")
    val title: String,
    @SerialName("输入")
    val input: String,
    @SerialName("输出")
    val output: String,
    @SerialName("思考")
    val thought: String,
    @SerialName("动作")
    val action: String,
    @SerialName("询问")
    val ask: String,
    @SerialName("最终输出")
    val finalOutput: String,
) {
}