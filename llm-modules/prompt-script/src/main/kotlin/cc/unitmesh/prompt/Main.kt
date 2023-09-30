package cc.unitmesh.prompt

import com.github.ajalt.clikt.core.CliktCommand

class Hello: CliktCommand() {
    override fun run() {
        echo("Hello World!")
    }
}

fun main(args: Array<String>) = Hello().main(args)
