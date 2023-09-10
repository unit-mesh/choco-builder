package cc.unitmesh.dsl.design

import cc.unitmesh.dsl.DesignLexer
import cc.unitmesh.dsl.DesignParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

class DesignDsl {
    fun analysis(design: String): DesignInformation {
        val context = this.parse(design).start()
        val listener = DesignAppListener()

        ParseTreeWalker().walk(listener, context)

        val nodeInfo = listener.getDesign()
        return nodeInfo
    }

    fun parse(str: String): DesignParser {
        val fromString = CharStreams.fromString(str)
        val lexer = DesignLexer (fromString)
        val tokenStream = CommonTokenStream(lexer)
        return DesignParser(tokenStream)
    }
}