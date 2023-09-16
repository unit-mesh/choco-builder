package cc.unitmesh.cf.language;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GuessLineCommentTest {
    private var languageService = LanguageService()

    @Test
    fun should_return_line_comment_when_language_exists() {
        // given
        val language = "Java"

        // when
        val lineComment = languageService.guessLineComment(language)

        // then
        assertThat(lineComment).isEqualTo("//")
    }

    @Test
    fun should_return_null_when_language_does_not_exist() {
        // given
        val language = "Python"

        // when
        val lineComment = languageService.guessLineComment(language)

        // then
        assertThat(lineComment).isEqualTo("#")
    }
}