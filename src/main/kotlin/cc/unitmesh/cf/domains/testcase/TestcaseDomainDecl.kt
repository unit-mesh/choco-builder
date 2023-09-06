package cc.unitmesh.cf.domains.testcase

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.workflow.Workflow
import org.springframework.stereotype.Component

@Component
class TestcaseDomainDecl : DomainDeclaration {
    override val domainName: String get() = "testcase"
    override val description: String get() = "根据用户的输入，生成测试用例"

    override fun workflow(question: String): Workflow {
        return TestcaseWorkflow()
    }
}