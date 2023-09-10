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
    val childComponents: List<DComponent>,
    val configs: Map<String, String>,
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
    val see: DSee,
    val `do`: DDo,
    val react: List<DReact>,
)

data class DFlow(
    val interactions: List<DInteraction>,
    val flowName: String,
)

data class DLayout(
    val layoutName: String,
    val layoutRows: List<DLayoutRow>,
)

data class DLayoutRow(
    val layoutCells: List<DLayoutCell>,
)

data class DLayoutCell(
    val componentName: String,
    val layoutInformation: String,
    val normalInformation: String,
)

data class DProperty(
    val key: String,
    val value: String,
)

data class LibraryPreset(
    val key: String,
    val value: String,
    val presetCalls: List<PresetCall>,
    val subProperties: List<DProperty>,
)

data class PresetCall(
    val libraryName: String,
    val libraryPreset: String,
)

data class DLibrary(
    val libraryName: String,
    val libraryPresets: List<LibraryPreset>,
)
