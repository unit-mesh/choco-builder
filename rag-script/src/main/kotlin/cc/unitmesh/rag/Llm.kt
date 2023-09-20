package cc.unitmesh.rag

class Llm(val prompt: String) {
    fun run() {
        println("llm $prompt")
    }

    fun request(function: () -> String) {
        function()
    }
}