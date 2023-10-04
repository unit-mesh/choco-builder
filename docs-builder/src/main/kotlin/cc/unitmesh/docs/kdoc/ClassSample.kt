package cc.unitmesh.docs.kdoc


data class FunctionSample(val name: String, val content: String, var code: String = "")
data class ClassSample(val nodeName: String, val samples: List<FunctionSample> = listOf())