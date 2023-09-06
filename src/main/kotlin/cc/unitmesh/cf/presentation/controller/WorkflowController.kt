package cc.unitmesh.cf.presentation.controller

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.domains.DomainClassify
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workflows")
class WorkflowController(val classify: DomainClassify) {
    val domains = classify.lookupDomains()

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(AgentController::class.java)
    }

    @GetMapping("/{domainName}")
    fun domainAgent(@PathVariable domainName: String): List<StageContext> {
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

    @GetMapping("/list")
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