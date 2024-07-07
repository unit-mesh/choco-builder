package cc.unitmesh.cf

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtUtil
import kotlin.collections.indices
import kotlin.collections.map
import kotlin.collections.slice
import kotlin.collections.toLongArray
import kotlin.ranges.until
import kotlin.to

open class LocalEmbedding(
    private val tokenizer: HuggingFaceTokenizer,
    private val session: OrtSession,
    private val env: OrtEnvironment,
) : Embedding {
    override fun getTokenizer(): HuggingFaceTokenizer {
        return tokenizer
    }

    override fun embed(input: String): List<Double> {
        val tokenized = tokenizer.encode(input, true, true)

        var inputIds = tokenized.ids
        var attentionMask = tokenized.attentionMask
        var typeIds = tokenized.typeIds

        if (tokenized.ids.size >= 512) {
            inputIds = inputIds.slice(0..510).toLongArray()
            attentionMask = attentionMask.slice(0..510).toLongArray()
            typeIds = typeIds.slice(0..510).toLongArray()
        }

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
        // floatArray is an inputIds.size * 384 array, we need to mean it to 384 * 1
        // 1, shape, shape.length
        val shapeSize = outputTensor.info.shape[2].toInt()
        val meanArray = FloatArray(shapeSize)
        for (i in 0 until shapeSize) {
            var sum = 0f
            for (j in inputIds.indices) {
                sum += floatArray[j * shapeSize + i]
            }

            meanArray[i] = sum / inputIds.size
        }

        return meanArray.map { it.toDouble() }
    }


    companion object {

        fun create(classLoader: ClassLoader = Thread.currentThread().getContextClassLoader()): LocalEmbedding {
            val tokenizer = loadTokenizer(classLoader)!!

            val ortEnv = OrtEnvironment.getEnvironment()
            val session = loadNetwork(classLoader, ortEnv)!!

            return LocalEmbedding(tokenizer, session, ortEnv)
        }

        /**
         * Loads a neural network model from the specified class loader and creates an OrtSession using the provided OrtEnvironment.
         *
         * @param classLoader the ClassLoader used to load the model file
         * @param ortEnv the OrtEnvironment used to create the OrtSession
         * @return the OrtSession created from the loaded model file, or null if an error occurs
         */
        fun loadNetwork(classLoader: ClassLoader, ortEnv: OrtEnvironment): OrtSession? {
            val sessionOptions = OrtSession.SessionOptions()

            val onnxStream = classLoader.getResourceAsStream("model/model.onnx")!!
            // load onnxPath as byte[]
            val onnxPathAsByteArray = onnxStream.readAllBytes()
            val session = ortEnv.createSession(onnxPathAsByteArray, sessionOptions)
            return session
        }

        /**
         * Loads a HuggingFaceTokenizer using the provided ClassLoader.
         *
         * @param classLoader the ClassLoader used to load the tokenizer resource
         * @return a HuggingFaceTokenizer instance loaded from the "model/tokenizer.json" resource, or null if the tokenizer could not be loaded
         */
        fun loadTokenizer(classLoader: ClassLoader): HuggingFaceTokenizer? {
            val tokenizerStream = classLoader.getResourceAsStream("model/tokenizer.json")
            val tokenizer = HuggingFaceTokenizer.newInstance(tokenizerStream, null)
            return tokenizer
        }
    }
}