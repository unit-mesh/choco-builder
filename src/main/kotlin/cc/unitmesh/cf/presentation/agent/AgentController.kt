package cc.unitmesh.cf.presentation.agent

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.prompt.PromptTemplate
import cc.unitmesh.cf.domains.DomainClassify
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agents")
class AgentController(
    val classify: DomainClassify,
) {
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

    @GetMapping("/domains/{domainName}")
    fun domainAgent(@PathVariable domainName: String): List<PromptTemplate> {
        val domains = classify.lookupDomains()
        log.info("domains: {}", domains)
        val domain = domains[domainName]
        if (domain == null) {
            log.warn("domain [{}] not found!", domainName)
            return emptyList()
        }
        val workflow = domain.workflow(domainName)

        return workflow.prompts.map {
            it.value
        }
    }

    // domains list
    @GetMapping("/domains")
    fun domains(): List<DomainResponse> {
        val domains: MutableMap<String, DomainDeclaration> = classify.lookupDomains()
        return domains.map {
            val clazz = it.value
            DomainResponse(clazz.domainName, clazz.description)
        }
    }
}

data class DomainResponse(
    val name: String,
    val description: String,
)