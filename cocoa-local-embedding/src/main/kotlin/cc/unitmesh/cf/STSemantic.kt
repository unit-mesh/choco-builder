package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtUtil
import java.net.URI
import java.nio.file.*
import java.util.*

/**
 * Sentence Transformers Semantic
 */
class STSemantic(val tokenizer: HuggingFaceTokenizer, val session: OrtSession, val env: OrtEnvironment) {
    fun embed(input: String): DoubleArray {
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

        val outputTensor = result.get(0) as OnnxTensor

        return outputTensor.doubleBuffer.array()
    }

    companion object {
        fun create(): STSemantic {
            val classLoader = Thread.currentThread().getContextClassLoader()

            val tokenizerPath = classLoader.getResource("model/tokenizer.json")!!.toURI()
            val onnxPath = classLoader.getResource("model/model.onnx")!!.toURI()

            try {
                val env: Map<String, String> = HashMap()
                val array: List<String> = tokenizerPath.toString().split("!")
                FileSystems.newFileSystem(URI.create(array[0]), env)
            } catch (e: Exception) {
//                e.printStackTrace()
            }

            val tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(tokenizerPath))
            val ortEnv = OrtEnvironment.getEnvironment()
            val sessionOptions = OrtSession.SessionOptions()

            // load onnxPath as byte[]
            val onnxPathAsByteArray = Files.readAllBytes(Paths.get(onnxPath))
            val session = ortEnv.createSession(onnxPathAsByteArray, sessionOptions)

            return STSemantic(tokenizer, session, ortEnv)
        }
    }
}