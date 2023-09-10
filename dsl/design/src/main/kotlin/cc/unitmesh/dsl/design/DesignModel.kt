package cc.unitmesh.dsl.design

data class DesignInformation(
    val projectConfigs: Map<String, String>,
    val flows: List<DFlow>,
    val components: Map<String, DComponent>,
    val layouts: List<DLayout>,
    val libraries: List<DLibrary>,
)

data class DConfig(
    val key: String,
    val value: String,
)

data class DComponent(
    val name: String,
    val childComponents: List<DComponent> = listOf(),
    var configs: Map<String, String> = mapOf(),
)

data class DSee(
    val componentName: String,
    val data: String,
)

data class DDo(
    val uiEvent: String,
    val componentName: String,
    val data: String,
)

data class DReact(
    val sceneName: String,
    val reactAction: String,
    val reactComponentName: String,
    val animateName: String,
    val reactComponentData: String,
)

data class DInteraction(
    var see: DSee,
    var `do`: DDo,
    val react: List<DReact>,
)

data class DFlow(
    var interactions: List<DInteraction>,
    val flowName: String,
)

data class DLayout(
    val layoutName: String,
    var layoutRows: List<DLayoutRow>,
)

data class DLayoutRow(
    var layoutCells: List<DLayoutCell>,
)

data class DLayoutCell(
    var componentName: String,
    var layoutInformation: String,
    var normalInformation: String,
)

data class DProperty(
    val key: String,
    val value: String,
)

data class LibraryPreset(
    var key: String,
    var value: String,
    val presetCalls: List<PresetCall>,
    val subProperties: List<DProperty>,
)

data class PresetCall(
    val libraryName: String,
    val libraryPreset: String,
)

data class DLibrary(
    var libraryName: String,
    val libraryPresets: List<LibraryPreset>,
)
