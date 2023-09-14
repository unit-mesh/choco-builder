package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtUtil

class STSemantic(
    private val tokenizer: HuggingFaceTokenizer,
    private val session: OrtSession,
    private val env: OrtEnvironment,
) : Semantic {
    override fun getTokenizer(): HuggingFaceTokenizer {
        return tokenizer
    }

    override fun embed(input: String): DoubleArray {
        val tokenized = tokenizer.encode(input, true)

        val inputIds = tokenized.ids
        val attentionMask = tokenized.attentionMask
        val typeIds = tokenized.typeIds

        val tensorInput = OrtUtil.reshape(inputIds, longArrayOf(1, inputIds.size.toLong()))
        val tensorAttentionMask = OrtUtil.reshape(attentionMask, longArrayOf(1, attentionMask.size.toLong()))
        val tensorTypeIds = OrtUtil.reshape(typeIds, longArrayOf(1, typeIds.size.toLong()))

        val result = session.run(
            mapOf(
                "input_ids" to OnnxTensor.createTensor(env, tensorInput),
                "attention_mask" to OnnxTensor.createTensor(env, tensorAttentionMask),
                "token_type_ids" to OnnxTensor.createTensor(env, tensorTypeIds),
            ),
        )

        val outputTensor: OnnxTensor = result.get(0) as OnnxTensor

        val floatArray = outputTensor.floatBuffer.array()
        val doubleBuffer = floatArray.map { it.toDouble() }


        return doubleBuffer.toDoubleArray()
    }

    companion object {
        fun create(): STSemantic {
            val classLoader = Thread.currentThread().getContextClassLoader()

            val tokenizerStream = classLoader.getResourceAsStream("model/tokenizer.json")!!
            val onnxStream = classLoader.getResourceAsStream("model/model.onnx")!!

            val tokenizer = HuggingFaceTokenizer.newInstance(tokenizerStream, null)
            val ortEnv = OrtEnvironment.getEnvironment()
            val sessionOptions = OrtSession.SessionOptions()

            // load onnxPath as byte[]
            val onnxPathAsByteArray = onnxStream.readAllBytes()
            val session = ortEnv.createSession(onnxPathAsByteArray, sessionOptions)

            return STSemantic(tokenizer, session, ortEnv)
        }
    }
}