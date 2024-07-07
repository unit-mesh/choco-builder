package cc.unitmesh.cf

import ai.djl.modality.nlp.DefaultVocabulary
import ai.djl.modality.nlp.Vocabulary
import ai.djl.modality.nlp.bert.BertToken
import ai.djl.modality.nlp.bert.BertTokenizer
import ai.djl.modality.nlp.qa.QAInput
import ai.djl.ndarray.NDList
import ai.djl.translate.TranslatorContext
import org.junit.jupiter.api.Test
import java.util.*


class DanBertTest {
    @Test
    fun shouldEmbeddingText() {
        val classLoader = Thread.currentThread().getContextClassLoader()

        val tokenizerStream = classLoader.getResource("bert-base-uncased.txt")

//         val file: Path = Paths.get("/YOUR PATH/vocab.txt")
        val vocabulary: Vocabulary = DefaultVocabulary.builder()
            .optMinFrequency(1)
            .addFromTextFile(tokenizerStream)
            .optUnknownToken("[UNK]")
            .build()

        val vocabSize = vocabulary.size()

        // do tokenizer
        val tokenizer = BertTokenizer()
//         val tokenizer = HuggingFaceTokenizer.newInstance(tokenizerStream, null)
    }

    fun processInput(ctx: TranslatorContext, tokenizer: BertTokenizer, vocabulary: Vocabulary, input: String): NDList {
        val token: BertToken = tokenizer.encode(input, input)
        // get the encoded tokens used in precessOutput
        val tokens = token.tokens
        val manager = ctx.ndManager
        // map the tokens(String) to indices(long)
        val indices: LongArray =
            tokens.stream().mapToLong(vocabulary::getIndex).toArray()
        val attentionMask =
            token.attentionMask.stream().mapToLong { i: Long? -> i!! }.toArray()
        val tokenType = token.tokenTypes.stream()
            .mapToLong { i: Long? -> i!! }.toArray()
        val indicesArray = manager.create(indices)
        val attentionMaskArray =
            manager.create(attentionMask)
        val tokenTypeArray = manager.create(tokenType)
        // The order matters
        return NDList(
            indicesArray, attentionMaskArray,
            tokenTypeArray
        )
    }
}