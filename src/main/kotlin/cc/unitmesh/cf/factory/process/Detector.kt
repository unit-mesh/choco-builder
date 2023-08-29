package cc.unitmesh.cf.factory.process

interface Detector {
    fun detect(question: String): List<String>
}

class DetectorPlaceholder : Detector {
    override fun detect(question: String): List<String> {
        return listOf()
    }
}