# Code Interpreter

Code Interpreter module based on Kotlin Jupyter Kernel: [https://github.com/Kotlin/kotlin-jupyter](https://github.com/Kotlin/kotlin-jupyter)

当前支持的主要三个扩展库:

* [dataframe](https://github.com/Kotlin/dataframe) - Kotlin framework for structured data processing.
* [kotless](https://github.com/JetBrains/kotless)  Kotlin Serverless Framework.
* [lets-plot](https://github.com/JetBrains/lets-plot-kotlin) - Kotlin API for Lets-Plot: multiplatform plotting library based on Grammar of Graphics.

Code Interpreter 作为一个独立的模块，可以被任何一个项目所使用。

### 1. 引入依赖后，要配置 bootJar

需要注意的是，由于要用动态的 Kotlin 运行时，需要在 [build.gradle.kts](./build.gradle.kts) 中配置 bootwar

如：

```kotlin
// allow script to unpack
// when spring boot start, those packages will unpack to some dir, so we can call it REPL.
tasks.withType<BootJar> {
    requiresUnpack("**/kotlin-compiler-*.jar")
    requiresUnpack("**/kotlin-script-*.jar")
    requiresUnpack("**/kotlin-scripting-*.jar")
    requiresUnpack("**/kotlin-jupyter-*.jar")
    requiresUnpack("**/lets-plot-*.jar")
    requiresUnpack("**/dataframe-*.jar")
    requiresUnpack("**/kotlinx-*.jar")
}
```

### 2. 配置 magic-number

为了直接使用 line magics，我们需要配置要使用的库。

目录：[ExtendLibraries.kt](src/main/kotlin/cc/unitmesh/code/interpreter/compiler/ExtendLibraries.kt)

诸如于：

```kotlin
val KotlessLibDef = SimpleLibraryDefinition(
    imports = listOf(
        "io.kotless.dsl.spring.*",
        "io.kotless.dsl.ktor.*",
        "io.kotless.dsl.lang.http.*",

        // todo: move to Kotlin libraries
        "kotlin.reflect.KClass",
        "kotlin.reflect.full.primaryConstructor"
    ),
    dependencies = listOf(
        "io.kotless:kotless-lang:0.2.0",
        "io.kotless:kotless-lang-local:0.2.0",
        "io.kotless:spring-boot-lang:0.2.0",
        "io.kotless:spring-boot-lang-local:0.2.0",
        "io.kotless:spring-lang-parser:0.2.0",
        "io.kotless:ktor-lang:0.2.0",
        "io.kotless:ktor-lang-local:0.2.0"
    ),
    repositories = listOf(
        "https://packages.jetbrains.team/maven/p/ktls/maven",
    ).map(::KernelRepository)
)
```

### 3. 输出结果定义

为了能够在前端展示，我们需要定义输出结果的格式。

示例代码：[KotlinInterpreter.kt](src/main/kotlin/cc/unitmesh/code/interpreter/KotlinInterpreter.kt)

```kotlin
when (resultValue) {
    // for Lets Plot
    is Plot -> {
        val content = PlotHtmlExport.buildHtmlFromRawSpecs(resultValue.toSpec(),
            PlotHtmlHelper.scriptUrl(VersionChecker.letsPlotJsVersion),
            plotSize = DoubleVector(600.0, 400.0)
        )

        return Message(
            id = id,
            resultValue = resultValue.toString(),
            className = className,
            displayValue = result.displayValue.toJson().toString(),
            msgType = MessageType.HTML,
            content = HtmlContent(content)
        )
    }
    //...
    else -> return Message(
        id,
        resultValue.toString(),
        className,
        result.displayValue.toJson().toString()
    )
}
```


