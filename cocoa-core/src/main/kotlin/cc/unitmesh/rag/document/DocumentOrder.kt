package cc.unitmesh.rag.document

object DocumentOrder {

    /**
     * Los in the middle reorder: the most relevant will be at the
     * middle of the list and more relevant elements at beginning / end.
     * See: https://arxiv.org/abs//2307.03172"""
     *
     * re-score codes by min in the middle, max in the beginning and end, for examples:
     * 0.6, 0.5, 0.4, 0.3, 0.2, 0.1 -> 0.6, 0.4, 0.2, 0.1, 0.3, 0.5
     */
    fun lostInMiddleReorder(documents: List<Pair<Double, String>>): List<Pair<Double, String>> {
        val reversedDocuments = documents.reversed()
        val reorderedResult = mutableListOf<Pair<Double, String>>()

        for ((index, value) in reversedDocuments.withIndex()) {
            if (index % 2 == 1) {
                reorderedResult.add(value)
            } else {
                reorderedResult.add(0, value)
            }
        }

        return reorderedResult
    }

}