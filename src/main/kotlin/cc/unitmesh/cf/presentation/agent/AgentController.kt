package cc.unitmesh.cf.presentation.agent

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agent")
class AgentController {
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

        return "TODO"
    }
}