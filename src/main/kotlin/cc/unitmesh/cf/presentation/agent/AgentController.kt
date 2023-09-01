package cc.unitmesh.cf.presentation.agent

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.domains.DomainClassify
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agent")
class AgentController(
    val classify: DomainClassify
) {

    @PostMapping("/smart")
    fun smartAgent(@RequestParam("q") query: String): String {
        // 1. find problem domain
        // 2. match problem domain to solution domain
        // 3. find solution domain
        // 4. execute solution
        return "TODO"
    }

    @PostMapping("/domain/{domainName}")
    fun domainAgent(@RequestParam("q") query: String, @PathVariable domainName: String): String {
        val domains = classify.lookupDomains()
        return "TODO"
    }

    // domains list
    @GetMapping("/domains")
    fun domains(): List<DomainResponse> {
        val domains: MutableMap<String, DomainDeclaration> = classify.lookupDomains()
        return domains.map {
            val clazz = it.value
            DomainResponse(clazz.name, clazz.description)
        }
    }
}

data class DomainResponse(
    val name: String,
    val description: String
)