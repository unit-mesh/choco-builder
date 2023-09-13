package cc.unitmesh.code.interpreter

import cc.unitmesh.code.interpreter.compiler.KotlinReplWrapper
import org.jetbrains.letsPlot.LetsPlot
import org.jetbrains.letsPlot.Stat
import org.jetbrains.letsPlot.core.util.PlotHtmlExport
import org.jetbrains.letsPlot.core.util.PlotHtmlHelper.scriptUrl
import org.jetbrains.letsPlot.export.VersionChecker
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.geom.geomText
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LetsPlotInterpreterTest {
    private lateinit var compiler: KotlinReplWrapper

    @BeforeEach
    internal fun setUp() {
        this.compiler = KotlinReplWrapper()
    }

    @Test
    internal fun local_lets_plot() {
        val incomeData = mapOf(
            "x" to listOf("一月", "二月", "三月", "四月", "五月", "六月"),
            "y" to listOf(201.2, 222, 234.3, 120.2, 90, 94.4)
        )
        val plot = letsPlot(incomeData) { x = "x"; y = "y" } +
                geomBar(stat = Stat.identity) +
                geomText(labelFormat = "\${.2f}") { label = "y"; } +
                ggtitle("2023 年上半年电费")

        // the html render
        val frontendContext = LetsPlot.setupNotebook("3.2.0", null) {}
        val html = frontendContext.getHtml(plot)
        println(html)

        val content = PlotHtmlExport.buildHtmlFromRawSpecs(plot.toSpec(), scriptUrl(VersionChecker.letsPlotJsVersion))
        println(content)
    }

    @Test
    internal fun simple_eval() {
        val code = """%use lets-plot

import kotlin.math.PI
import kotlin.random.Random

val incomeData = mapOf(
    "x" to listOf("一月", "二月", "三月", "四月", "五月", "六月"),
    "y" to listOf(201.2, 222, 234.3, 120.2, 90, 94.4)
)
letsPlot(incomeData) { x = "x"; y = "y" } +
        geomBar(stat = Stat.identity) +
        geomText(labelFormat = "\${'$'}{.2f}") { label = "y"; } +
        ggtitle("2023 年上半年电费")
"""

        compiler.eval(code)
    }

    @Test
    internal fun simple_eval2() {
        val code = """%use lets-plot
import kotlin.math.PI
import kotlin.random.Random

val electricityData = mapOf(
   "Month" to listOf("一月", "二月", "三月", "四月", "五月", "六月"),
   "Expenditure" to listOf(201.2, 222, 234.3, 120.2, 90, 90.4)
)

letsPlot(electricityData) { x = "Month"; y = "Expenditure" } +
   geomLine() +
   geomPoint() +
   ggtitle("2023 年上半年电费趋势")
"""
        val res = compiler.eval(code)
        val plot: Plot = res.rawValue as Plot
        ggsave(plot, "plot.png")
    }
}