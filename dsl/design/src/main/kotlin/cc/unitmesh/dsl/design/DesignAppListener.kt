package cc.unitmesh.dsl.design

import cc.unitmesh.dsl.DesignBaseListener
import cc.unitmesh.dsl.DesignParser

class DesignAppListener : DesignBaseListener() {
    private val projectConfigs: MutableMap<String, String> = mutableMapOf()
    private val components: MutableMap<String, DComponent> = mutableMapOf()
    private val flows: MutableList<DFlow> = mutableListOf()
    private val layouts: MutableList<DLayout> = mutableListOf()
    private val libraries: MutableList<DLibrary> = mutableListOf()

    override fun enterConfigDeclaration(ctx: DesignParser.ConfigDeclarationContext?) {
        projectConfigs[ctx!!.configKey().text] = ctx.configValue().text
    }

    override fun enterFlowDeclaration(ctx: DesignParser.FlowDeclarationContext?) {
        val interactions = buildInteractions(ctx!!.interactionDeclaration())
        val flow = DFlow(
            interactions = interactions,
            flowName = ctx.IDENTIFIER().text,
        )
        flows.add(flow)
    }

    private fun buildInteractions(declarationContexts: List<DesignParser.InteractionDeclarationContext>): List<DInteraction> {
        val interactions: MutableList<DInteraction> = mutableListOf()
        var interaction = createInteraction()

        declarationContexts.forEach { context ->
            when (val child = context.getChild(0)) {
                is DesignParser.SeeDeclarationContext -> {
                    var componentData = ""
                    val componentName: String
                    if (child.IDENTIFIER() != null) {
                        componentName = child.IDENTIFIER().text
                    } else {
                        componentName = child.componentName().text
                        componentData = removeQuote(child.STRING_LITERAL().text)
                    }

                    if (interaction.see.componentName != "") {
                        interactions += interaction
                    }

                    interaction = createInteraction()
                    interaction.see = DSee(
                        componentName = componentName,
                        data = componentData,
                    )
                }

                is DesignParser.DoDeclarationContext -> {
                    val doModel = DDo(
                        componentName = child.componentName().text,
                        data = child.STRING_LITERAL().text,
                        uiEvent = child.actionName().text,
                    )
                    interaction.`do` = doModel
                }

                is DesignParser.ReactDeclarationContext -> {
                    var sceneName = ""
                    if (child.sceneName() != null) {
                        sceneName = child.sceneName().text
                    }
                    var animateName = ""
                    if (child.animateDeclaration() != null) {
                        animateName = child.animateDeclaration().animateName().text
                    }

                    val (actionName, reactComponentName, reactComponentData) = buildAction(child)

                    val reactModel = DReact(
                        sceneName = sceneName,
                        reactAction = actionName,
                        reactComponentName = reactComponentName,
                        reactComponentData = removeQuote(reactComponentData),
                        animateName = animateName,
                    )

                    interaction.react += reactModel
                }
            }
        }

        interactions += interaction
        return interactions
    }

    private fun buildAction(reactCtx: DesignParser.ReactDeclarationContext): Triple<String, String, String> {
        var actionName = ""
        var reactComponentName = ""
        var reactComponentData = ""

        when (val firstChild = reactCtx.reactAction().getChild(0)) {
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
        val component = DComponent(componentName)
        components.computeIfAbsent(componentName) { component }

        val componentConfigs: MutableMap<String, String> = mutableMapOf()
        ctx.componentBodyDeclaration().forEach { declaration ->
            declaration.children.forEach {
                when (it) {
                    is DesignParser.ComponentNameContext -> {
                        val childComponent = DComponent(it.text)
                        component.childComponents += childComponent
                    }

                    is DesignParser.ConfigKeyContext -> {
                        val configValue = declaration.getChild(2) as DesignParser.ConfigValueContext
                        val configValueText = removeQuote(configValue.text)
                        componentConfigs[it.text] = configValueText
                    }
                }
            }
        }

        component.configs = componentConfigs
        components[componentName] = component
    }

    override fun enterLayoutDeclaration(ctx: DesignParser.LayoutDeclarationContext?) {
        val layout = DLayout(ctx!!.IDENTIFIER().text, emptyList())
        val layoutRows = ctx.layoutRow()

        layout.layoutRows = parseLayoutRows(layoutRows)

        layouts += layout
    }

    private fun parseLayoutRows(layoutRows: MutableList<DesignParser.LayoutRowContext>) =
        layoutRows.map { row ->
            val layoutRow = DLayoutRow(emptyList())
            if (row.getChild(0) !is DesignParser.LayoutLinesContext) {
                return@map null
            }

            val lines = (row.getChild(0) as DesignParser.LayoutLinesContext).layoutLine()
            layoutRow.layoutCells = lines.map { line ->
                val cell = DLayoutCell("", "", "")
                val declaration = line.componentUseDeclaration()
                parseLayoutLine(declaration, cell)
                cell
            }

            layoutRow
        }.filterNotNull()

    override fun enterSimpleLayoutDeclaration(ctx: DesignParser.SimpleLayoutDeclarationContext?) {
        layouts += DLayout("", parseLayoutRows(ctx!!.layoutRow().toMutableList()))
    }

    private fun parseLayoutLine(declaration: DesignParser.ComponentUseDeclarationContext, cell: DLayoutCell) {
        when (val firstChild = declaration.getChild(0)) {
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
            when (express) {
                is DesignParser.PresetKeyValueContext -> {
                    val preset = LibraryPreset(key = "", value = "")
                    preset.key = express.presetKey().text
                    preset.value = express.presetValue().text

                    library.libraryPresets += preset
                }

                is DesignParser.PresetKeyObjectContext -> {
                    val preset = LibraryPreset(key = "", value = "")
                    preset.key = express.presetKey().text
                    for (keyValue in express.keyValue()) {
                        val key = keyValue.presetKey().text
                        var value = keyValue.presetValue().text
                        value = removeQuote(value)

                        preset.subProperties += DProperty(key, value)
                    }

                    library.libraryPresets += preset
                }

                is DesignParser.PresetKeyArrayContext -> {
                    val preset = LibraryPreset(key = "", value = "")
                    preset.key = express.presetKey().text
                    for (presetCall in express.presetCall()) {
                        preset.inheritProps += DProperty(
                            key = presetCall.libraryName().text,
                            value = presetCall.IDENTIFIER().text,
                        )
                    }

                    library.libraryPresets += preset
                }
            }
        }

        libraries += library
    }

    fun getDesign(): DesignInformation = DesignInformation(
        projectConfigs = projectConfigs,
        flows = flows,
        components = components,
        layouts = layouts,
        libraries = libraries,
    )
}

fun removeQuote(value: String): String {
    return value.removeSurrounding("\"")
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