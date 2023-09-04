package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.prompt.PromptTemplate
import cc.unitmesh.cf.domains.DomainClassify
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agents")
class AgentController(val classify: DomainClassify) {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(AgentController::class.java)
    }

    @PostMapping("/smart")
    fun smartAgent(@RequestParam("q") query: String): String {
        // 1. find problem domain
        // 2. match problem domain to solution domain
        // 3. find solution domain
        // 4. execute solution
        return "TODO"
    }
}
