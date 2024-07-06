package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession

@Deprecated("Use LocalEmbedding instead")
class STEmbedding(
    private val tokenizer: HuggingFaceTokenizer,
    private val session: OrtSession,
    private val env: OrtEnvironment,
) : LocalEmbedding(tokenizer, session, env) {

    companion object {
        fun create(): LocalEmbedding {
            return LocalEmbedding.create()
        }
    }
}
