package cc.unitmesh.cf.presentation

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agent")
class AgentController {
    @PostMapping("/explain-biz")
    // todo: align to OpenAPI format and return Flow<String>
    fun explainBiz(): String {
        // 1. find problem domain
        // 2. match problem domain to solution domain
        // 3. find solution domain
        // 4. execute solution
        return "TODO"
    }
}
