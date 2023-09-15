package cc.unitmesh.code.magic.lang

class KtorLangBuilder(private val code: String, private val port: Int): LangBuilder {
    override fun build(): String {
        return """$code

fun main() {
    val classToStart = Server::class.java.name

    val ktClass = ::main::class.java.classLoader.loadClass(classToStart).kotlin
    val instance = (ktClass.primaryConstructor?.call() ?: ktClass.objectInstance) as? KotlessAWS

    val kotless = instance ?: error("instance inherit from Kotless!")

    embeddedServer(Netty, ${port}) {
        kotless.prepare(this)
    }.start(wait = true)
}

main()
"""
    }
}
