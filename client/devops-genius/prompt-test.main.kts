@file:DependsOn("cc.unitmesh:rag-script:0.3.7")
@file:DependsOn("cc.unitmesh:prompt-script:0.3.7")

import cc.unitmesh.prompt.executor.ScriptExecutor
import java.io.File


val file = File("config/prompt.unit-mesh.yml")
if (!file.exists()) {
    throw Exception("input file not found: $file")
}

val executor = ScriptExecutor(file)
executor.execute()
