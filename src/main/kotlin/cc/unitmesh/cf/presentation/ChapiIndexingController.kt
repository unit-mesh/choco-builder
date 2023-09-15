package cc.unitmesh.cf.presentation

import chapi.domain.core.CodeDataStruct
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scanner/{systemId}/reporting")
class ChapiIndexingController {
    @PostMapping("/class-items")
    fun saveClassItems(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<CodeDataStruct>,
    ) {
        // TODO
    }
}