package cc.unitmesh.cf.domains.semantic.model

class SemanticVariables(
    var englishQuery: String,
    var originLanguageQuery: String,
    var hypotheticalDocument: String,
    var relevantCode: List<String>,
)