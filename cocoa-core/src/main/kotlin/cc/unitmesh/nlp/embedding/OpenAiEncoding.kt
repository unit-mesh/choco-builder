package cc.unitmesh.nlp.embedding

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.EncodingType

class OpenAiEncoding : EncodingTokenizer {
    private val registry: EncodingRegistry = Encodings.newLazyEncodingRegistry()
    private val encoding: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)
    override fun encode(text: String): List<Int> {
        return encoding.encode(text)
    }

    override fun decode(tokens: List<Int>): String {
        return encoding.decode(tokens)
    }
}
