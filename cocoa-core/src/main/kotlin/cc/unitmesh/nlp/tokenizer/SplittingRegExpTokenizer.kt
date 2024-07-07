package cc.unitmesh.nlp.tokenizer

class SplittingRegExpTokenizer : Tokenizer {
    private val SEPARATOR = "~"

    private object SplittingRegExps {
        val wordsEndingLocations: List<Regex> = arrayOf(
            "(?<=[A-Za-z])(?=[A-Z][a-z])", "[^\\w\\s]", "[_\\-]"
        ).map { it.toRegex() }

        val boundDigitsLocation: Regex = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)".toRegex()
    }

    override fun tokenize(identifier: String): List<String> {
        var transformedIdentifier = identifier
        for (regex in SplittingRegExps.wordsEndingLocations) {
            transformedIdentifier = transformedIdentifier.replace(regex, SEPARATOR)
        }

        return buildList {
            for (token in transformedIdentifier.split(SEPARATOR)) {
                if (token.isEmpty()) continue
                var isNextCharUpperCase = Character.isUpperCase(token.last())
                var transformedToken = token

                for (index in token.length - 2 downTo 0) {
                    val isCurCharUpperCase = Character.isUpperCase(transformedToken[index])
                    val isCaseChanging = isNextCharUpperCase xor isCurCharUpperCase
                    if (isCaseChanging) {
                        val splitPosition = index + if (isNextCharUpperCase) 1 else 0
                        transformedToken =
                            transformedToken.substring(0, splitPosition) + SEPARATOR + transformedToken.substring(
                                splitPosition
                            )
                        isNextCharUpperCase = isCurCharUpperCase
                    }
                }

                transformedToken.split(SEPARATOR)
                    .flatMap { it.split(SplittingRegExps.boundDigitsLocation) }
                    .filterNot(String::isEmpty)
                    .map { it.lowercase() }
                    .forEach(this::add)
            }
        }
    }
}