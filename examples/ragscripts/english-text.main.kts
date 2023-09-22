@file:DependsOn("cc.unitmesh:rag-script:0.3.3")

import cc.unitmesh.rag.*

rag {
    indexing {
        val chunks = text("fun main(args: Array<String>) {\n    println(\"Hello, World!\")\n}").split()
        store.indexing(chunks)
    }

    querying {
        store.findRelevant("Hello World")
    }
}