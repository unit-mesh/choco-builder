package cc.unitmesh.cf.presentation.controller;

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
        val dslOutput = DslOutput("name", "description", "output", "dsl")

        `when`(dslCompiler.compile(SimpleCompilerDsl(dslName, dslRequest.dsl))).thenReturn(dslOutput)

        mockMvc.perform(
            post("/dsl/{dslName}", dslName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dsl\":\"dsl\"}")
        )
            .andExpect(status().isOk)
    }
}