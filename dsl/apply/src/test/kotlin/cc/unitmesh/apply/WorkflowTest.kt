package cc.unitmesh.apply

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import kotlin.test.Test

class WorkflowTest {
    @Test
    fun hello_apply() {
        apply("code") {
            connection = Connection("openai")

            prepare {
                val file = Http.download("https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar") {

                }

                val output = Exec("java -jar ${file.absolutePath} -h").output { it }
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
