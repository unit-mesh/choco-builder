package cc.unitmesh.docs.model

abstract class DocGenerator {

    open fun execute(): List<RootDocContent> {
        return listOf()
    }
}