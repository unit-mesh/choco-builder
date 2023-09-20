package cc.unitmesh.apply

class Llm(val prompt: String) {
    fun run() {
        println("llm $prompt")
    }

    fun request(function: () -> String) {
        function()
    }
}