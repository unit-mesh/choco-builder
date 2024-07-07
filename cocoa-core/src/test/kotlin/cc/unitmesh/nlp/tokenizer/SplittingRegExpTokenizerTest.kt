package cc.unitmesh.nlp.tokenizer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SplittingRegExpTokenizerTest {

    @Test
    fun should_splitIdentifierIntoTokens_whenValidIdentifierProvided() {
        // given
        val tokenizer = SplittingRegExpTokenizer()
        val identifier = "splittingRegExpTokenizer"

        // when
        val result = tokenizer.tokenize(identifier)

        // then
        assertEquals(listOf("splitting", "reg", "exp", "tokenizer"), result)
    }

    @Test
    fun should_splitIdentifierIntoTokens_whenIdentifierContainsSpecialCharacters() {
        // given
        val tokenizer = SplittingRegExpTokenizer()
        val identifier = "splitting_RegExp-Tokenizer"

        // when
        val result = tokenizer.tokenize(identifier)

        // then
        assertEquals(listOf("splitting", "reg", "exp", "tokenizer"), result)
    }

    @Test
    fun should_splitIdentifierIntoTokens_whenIdentifierContainsNumbers() {
        // given
        val tokenizer = SplittingRegExpTokenizer()
        val identifier = "splittingRegExpTokenizer123"

        // when
        val result = tokenizer.tokenize(identifier)

        // then
        assertEquals(listOf("splitting", "reg", "exp", "tokenizer", "123"), result)
    }

    @Test
    fun should_splitIdentifierIntoTokens_whenIdentifierContainsUpperCaseLetters() {
        // given
        val tokenizer = SplittingRegExpTokenizer()
        val identifier = "SplittingRegExpTokenizer"

        // when
        val result = tokenizer.tokenize(identifier)

        // then
        assertEquals(listOf("splitting", "reg", "exp", "tokenizer"), result)
    }
}
