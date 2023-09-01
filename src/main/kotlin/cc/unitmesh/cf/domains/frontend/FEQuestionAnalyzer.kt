package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.process.QuestionAnalyzer
import cc.unitmesh.cf.core.process.related.RelatedQueryService
import cc.unitmesh.cf.domains.frontend.dsl.FEDslContextBuilder
import cc.unitmesh.cf.infrastructure.llms.completion.CompletionProvider

class FEQuestionAnalyzer(
    private val queryService: RelatedQueryService,
    private val contextBuilder: FEDslContextBuilder,
    private val completion: CompletionProvider,
) : QuestionAnalyzer {
    override fun analyze(domain: String, question: String): Dsl {
        TODO()
    }
}