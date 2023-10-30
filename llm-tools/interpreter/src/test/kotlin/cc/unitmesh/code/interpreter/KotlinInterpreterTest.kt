package cc.unitmesh.code.interpreter

import cc.unitmesh.code.interpreter.api.InterpreterRequest
import org.junit.jupiter.api.Test

class KotlinInterpreterTest {
    private val compiler = KotlinInterpreter()

    @Test
    internal fun should_catch_issue_for_errored_code() {
        val request: InterpreterRequest = InterpreterRequest(
            code = "val x = 3; y*2",
            language = "kotlin",
        )

        val res = compiler.eval(request)
    }

    @Test
    internal fun should_handle_rag_script() {
        val request: InterpreterRequest = InterpreterRequest(
            code = """@file:DependsOn("cc.unitmesh:rag-script:0.3.3")

                 rag("code") {
            // 使用 OpenAI 作为 LLM 引擎
            llm = LlmConnector(LlmType.OpenAI)
            // 使用 SentenceTransformers 作为 Embedding 引擎
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            // 使用 Memory 作为 Retriever
            store = Store(StoreType.Memory)

            indexing {
                // 从文件中读取文档
                val document = document("filename.txt")
                // 将文档切割成 chunk
                val chunks = document.split()
                // 建立索引
                store.indexing(chunks)
            }

            querying {
                // 查询
                store.findRelevant("workflow dsl design ").also {
                    println(it)
                }
            }
        }
            """.trimIndent(),
            language = "kotlin",
        )

        val res = compiler.eval(request)
    }
}