package cc.unitmesh.dsl.design

import cc.unitmesh.dsl.DesignBaseListener
import cc.unitmesh.dsl.DesignParser

class DesignAppListener() : DesignBaseListener() {
    private val projectConfigs: MutableMap<String, String> = mutableMapOf()
    private val components: MutableMap<String, DComponent> = mutableMapOf()
    private val flows: MutableList<DFlow> = mutableListOf()
    private val layouts: MutableList<DLayout> = mutableListOf()
    private val libraries: MutableList<DLibrary> = mutableListOf()

    override fun enterConfigDeclaration(ctx: DesignParser.ConfigDeclarationContext?) {
        projectConfigs[ctx!!.configKey().text] = ctx.configValue().text
    }

    override fun enterFlowDeclaration(ctx: DesignParser.FlowDeclarationContext?) {
        val flowName = ctx!!.IDENTIFIER().text
        val flow = DFlow(
            interactions = emptyList(),
            flowName = flowName,
        )

        val declarationContexts = ctx.interactionDeclaration()

        val interactions = buildInteractions(declarationContexts, emptyList())

        flow.interactions = interactions
        flows.add(flow)
    }

    fun buildInteractions(
        declarationContexts: List<DesignParser.InteractionDeclarationContext>,
        interactions: List<DInteraction>,
    ): List<DInteraction> {
        var interaction = createInteraction()

        for (context in declarationContexts) {
            val childTypes = context.getChild(0).javaClass.name

            when (childTypes) {
                "cc.unitmesh.dsl.DesignParser\$SeeDeclarationContext" -> {
                    val seeCtx = context.getChild(0) as DesignParser.SeeDeclarationContext
                    var componentName = ""
                    var componentData = ""
                    if (seeCtx.IDENTIFIER() != null) {
                        componentName = seeCtx.IDENTIFIER().text
                    } else {
                        componentName = seeCtx.componentName().text
                        componentData = removeQuote(seeCtx.STRING_LITERAL().text)
                    }
                    val seeModel = DSee(
                        componentName = componentName,
                        data = componentData,
                    )

                    if (interaction.see.componentName != "") {
                        interactions + interaction
                    }
                    interaction = createInteraction()
                    interaction.see = seeModel
                }

                "cc.unitmesh.dsl.DesignParser\$DoDeclarationContext" -> {
                    val doCtx = context.getChild(0) as DesignParser.DoDeclarationContext
                    val doModel = DDo(
                        componentName = doCtx.componentName().text,
                        data = doCtx.STRING_LITERAL().text,
                        uiEvent = doCtx.actionName().text,
                    )
                    interaction.`do` = doModel
                }

                "cc.unitmesh.dsl.DesignParser\$ReactDeclarationContext" -> {
                    val reactCtx = context.getChild(0) as DesignParser.ReactDeclarationContext
                    var sceneName = ""
                    if (reactCtx.sceneName() != null) {
                        sceneName = reactCtx.sceneName().text
                    }
                    var animateName = ""
                    if (reactCtx.animateDeclaration() != null) {
                        animateName = reactCtx.animateDeclaration().animateName().text
                    }

                    val (actionName, reactComponentName, reactComponentData) = buildAction(reactCtx)

                    val reactModel = DReact(
                        sceneName = sceneName,
                        reactAction = actionName,
                        reactComponentName = reactComponentName,
                        reactComponentData = reactComponentData,
                        animateName = animateName,
                    )
                    interaction.react + reactModel
                }
            }
        }

        interactions + interaction
        return interactions
    }

    private fun buildAction(reactCtx: DesignParser.ReactDeclarationContext): Triple<String, String, String> {
        var actionName = ""
        var reactComponentName = ""
        var reactComponentData = ""
        val firstChild = reactCtx.reactAction().getChild(0)
        when (firstChild) {
            is DesignParser.ShowActionContext -> {
                reactComponentData = firstChild.STRING_LITERAL().text
                reactComponentName = firstChild.componentName().text
                actionName = firstChild.SHOW_KEY().text
            }

            is DesignParser.GotoActionContext -> {
                reactComponentName = firstChild.componentName().text
                actionName = firstChild.GOTO_KEY().text
            }
        }

        return Triple(actionName, reactComponentName, reactComponentData)
    }

    override fun enterComponentDeclaration(ctx: DesignParser.ComponentDeclarationContext?) {
        val componentName = ctx!!.IDENTIFIER().text
        val component = components[componentName]
        if (component?.name == "") {
            components[componentName] = DComponent(componentName)
        }
        val componentConfigs: MutableMap<String, String> = mutableMapOf()
        val declarations = ctx.componentBodyDeclaration()
        for (declaration in declarations) {
            val childTypes = declaration.getChild(0).javaClass.name

            when (childTypes) {
                "cc.unitmesh.dsl.DesignParser\$ComponentNameContext" -> {
                    val childCtx = declaration.getChild(0) as DesignParser.ComponentNameContext
                    val childComponent = DComponent(childCtx.text)
                    component!!.childComponents + childComponent
                }

                "cc.unitmesh.dsl.DesignParser\$ConfigKeyContext" -> {
                    val configKey = declaration.getChild(0) as DesignParser.ConfigKeyContext
                    val configValue = declaration.getChild(2) as DesignParser.ConfigValueContext

                    val configValueText = removeQuote(configValue.text)

                    componentConfigs[configKey.text] = configValueText
                }
            }
        }

        component!!.configs = componentConfigs
        components[componentName] = component
    }

    override fun enterLayoutDeclaration(ctx: DesignParser.LayoutDeclarationContext?) {
        val layout = DLayout(ctx!!.IDENTIFIER().text, emptyList())
        ctx.layoutRow().forEach { row ->
            if (row.getChild(0) !is DesignParser.LayoutLinesContext) {
                return@forEach
            }

            val lines = (row.getChild(0) as DesignParser.LayoutLinesContext).layoutLine()
            val layoutRow = DLayoutRow(emptyList())

            lines.forEach { line ->
                val cell = DLayoutCell("", "", "")
                val declaration = line.componentUseDeclaration()
                parseLayoutLine(declaration, cell)

                layoutRow.layoutCells += cell
            }

            layout.layoutRows += layoutRow
        }

        layouts += layout
    }

    private fun parseLayoutLine(declaration: DesignParser.ComponentUseDeclarationContext, cell: DLayoutCell) {
        val firstChild = declaration.getChild(0)
        when (firstChild) {
            is DesignParser.ComponentNameContext -> {
                val componentName = firstChild.IDENTIFIER().text
                var layoutValue = ""
                if (declaration.childCount > 2) {
                    layoutValue = declaration.getChild(2).text
                }
                cell.componentName = componentName
                cell.layoutInformation = removeQuote(layoutValue)
            }

            else -> {
                val componentValue = firstChild.text
                cell.componentName = removeQuote(componentValue)
                cell.normalInformation = removeQuote(componentValue)
            }
        }
    }

    override fun enterLibraryDeclaration(ctx: DesignParser.LibraryDeclarationContext?) {
        val library = DLibrary(libraryName = "", libraryPresets = emptyList())
        library.libraryName = ctx!!.libraryName().text

        for (express in ctx.libraryExpress()) {
            val preset = LibraryPreset(
                key = "",
                value = "",
                presetCalls = emptyList(),
                subProperties = emptyList(),
            )
            preset.key = express.presetKey().text
            val pairCtx = express.getChild(2)
            when (pairCtx) {
                is DesignParser.PresetValueContext -> {
                    preset.value = pairCtx.text
                }

                is DesignParser.PresetArrayContext -> {
                    for (call in pairCtx.presetCall()) {
                        val presetCall = PresetCall(
                            libraryName = call.libraryName().text,
                            libraryPreset = call.IDENTIFIER().text,
                        )

                        preset.presetCalls + presetCall
                    }
                }

                is DesignParser.KeyValueContext -> {
                    for (keyValue in express.keyValue()) {
                        val key = keyValue.presetKey().text
                        var value = keyValue.presetValue().text

                        value = removeQuote(value)

                        preset.subProperties + DProperty(key, value)
                    }
                }
            }

            library.libraryPresets + preset
        }

        libraries + library
    }

    fun getDesign(): DesignInformation {
        return DesignInformation(
            projectConfigs = projectConfigs,
            flows = flows,
            components = components,
            layouts = layouts,
            libraries = libraries,
        )
    }

}

fun removeQuote(value: String): String {
    return value.replace("\"", "")
}

fun createInteraction(): DInteraction {
    val seeModel = DSee(
        componentName = "",
        data = "",
    )
    val doModel = DDo(
        componentName = "",
        data = "",
        uiEvent = "",
    )
    return DInteraction(
        see = seeModel,
        `do` = doModel,
        react = emptyList(),
    )
}