package cc.unitmesh.cf.presentation;

import cc.unitmesh.cf.core.dsl.DslCompiler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(DslController::class)
@AutoConfigureMockMvc
class DslControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var dslCompiler: DesignCompiler

    @Test
    fun shouldCompileDsl() {
        val dslName = DslName.Design
        val dslRequest = DslRequest("""--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------""")

        mockMvc.perform(
            post("/dsl/{dslName}", dslName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(dslRequest))
        )
            .andExpect(status().isOk)
    }
}
