package cc.unitmesh.apply

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.rag.store.EmbeddingMatch
import kotlin.test.Test

class WorkflowTest {
    @Test
    fun hello_apply() {
        apply("code") {
            prepare {
                val data: Any = http.get("https://www.baidu.com") {
                    println("hello")
                }

                // to json
                val chunks = document("filename").split()
                embedding().indexing(chunks)
            }
            problem {
                val dsl = {
                    // json schema ?
                }

                return@problem object: Dsl {
                    override var domain: String = ""
                    override val content: String = ""
                    override var interpreters: List<DslInterpreter> = listOf()
                }
            }
            solution {
                val results = embedding().query("")
                val sorted = results
                    .lowInMiddle()

                llm("openai").request {
                    // add prompt it here
                    """a"""
                }
            }
        }
    }
}

// TODO: add order by score value
private fun <T> Iterable<EmbeddingMatch<T>>.lowInMiddle(): List<EmbeddingMatch<T>> {
    val reversedDocuments = this.reversed()
    val reorderedResult = mutableListOf<EmbeddingMatch<T>>()

    for ((index, value) in reversedDocuments.withIndex()) {
        if (index % 2 == 1) {
            reorderedResult.add(value)
        } else {
            reorderedResult.add(0, value)
        }
    }

    return reorderedResult
}
