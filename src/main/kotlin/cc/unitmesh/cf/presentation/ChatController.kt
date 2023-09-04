package cc.unitmesh.cf.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController {
    @PostMapping("/chat")
    fun chat(): String {
        return "Hello World!"
    }
}