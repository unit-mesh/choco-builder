package cc.unitmesh.nlp.embedding

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.EncodingType
import com.knuddels.jtokkit.api.IntArrayList

class OpenAiEncoding : EncodingTokenizer {
    private val registry: EncodingRegistry = Encodings.newLazyEncodingRegistry()
    private val encoding: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)

    override fun encode(text: String): List<Int> {
        val encode: IntArrayList = encoding.encode(text)
        return encode.boxed()
    }

    override fun decode(tokens: List<Int>): String {
        val intArray: IntArrayList = IntArrayList(tokens.size)
        tokens.forEach { intArray.add(it) }
        return encoding.decode(intArray)
    }
}
