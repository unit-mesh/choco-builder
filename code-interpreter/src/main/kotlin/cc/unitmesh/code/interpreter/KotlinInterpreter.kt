package cc.unitmesh.code.interpreter

import cc.unitmesh.code.interpreter.api.InterpreterRequest
import cc.unitmesh.code.messaging.ErrorContent
import cc.unitmesh.code.messaging.Message
import cc.unitmesh.code.messaging.MessageType
import cc.unitmesh.code.interpreter.compiler.KotlinReplWrapper
import cc.unitmesh.code.messaging.HtmlContent
import org.jetbrains.kotlinx.jupyter.api.toJson
import org.jetbrains.kotlinx.jupyter.repl.EvalResultEx
import org.jetbrains.letsPlot.core.util.PlotHtmlExport
import org.jetbrains.letsPlot.core.util.PlotHtmlHelper
import org.jetbrains.letsPlot.export.VersionChecker
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.intern.toSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KotlinInterpreter {
    private var compiler: KotlinReplWrapper = KotlinReplWrapper()
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun eval(request: InterpreterRequest): Message {
        return try {
            val resultEx = compiler.eval(request.code, request.id, request.history)
            convertResult(resultEx, request.id)
        } catch (e: Exception) {
            logger.error(e.toString())
            val content = ErrorContent(e.javaClass.name, e.toString())
            Message(request.id, "", "", "", MessageType.ERROR, content = content)
        }
    }

    private fun convertResult(result: EvalResultEx, id: Int): Message {
        val resultValue = result.rawValue
        val className: String = resultValue?.javaClass?.name.orEmpty()

        if (resultValue is Plot) {
            val content = PlotHtmlExport.buildHtmlFromRawSpecs(resultValue.toSpec(),
                PlotHtmlHelper.scriptUrl(VersionChecker.letsPlotJsVersion)
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

        return Message(
            id,
            resultValue.toString(),
            className,
            result.displayValue.toJson().toString()
        )
    }
}