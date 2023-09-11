package cc.unitmesh.dsl.design

import kotlinx.serialization.Serializable

@Serializable
data class DesignInformation(
    val projectConfigs: Map<String, String>,
    val flows: List<DFlow>,
    val components: Map<String, DComponent>,
    val layouts: List<DLayout>,
    val libraries: List<DLibrary>,
)

@Serializable
data class DConfig(
    val key: String,
    val value: String,
)

@Serializable
data class DComponent(
    val name: String,
    var child: List<DComponent> = listOf(),
    var configs: Map<String, String> = mapOf(),
)

@Serializable
data class DSee(
    val componentName: String,
    val data: String,
)

@Serializable
data class DDo(
    val uiEvent: String,
    val componentName: String,
    val data: String,
)

@Serializable
data class DReact(
    val sceneName: String,
    val reactAction: String,
    val reactComponentName: String,
    val animateName: String,
    val reactComponentData: String,
)

@Serializable
data class DInteraction(
    var see: DSee,
    var `do`: DDo,
    var react: List<DReact>,
)

@Serializable
data class DFlow(
    var interactions: List<DInteraction>,
    val flowName: String,
)

@Serializable
data class DLayout(
    val layoutName: String,
    var layoutRows: List<DLayoutRow>,
)

@Serializable
data class DLayoutRow(
    var layoutCells: List<DLayoutCell>,
)

@Serializable
data class DLayoutCell(
    var componentName: String,
    var layoutInformation: String,
    var normalInformation: String,
)

@Serializable
data class DProperty(
    val key: String,
    val value: String,
)

@Serializable
data class LibraryPreset(
    var key: String,
    var value: String,
    var presetCalls: List<PresetCall> = listOf(),
    var subProperties: List<DProperty> = listOf(),
    var inheritProps: List<DProperty> = listOf(),
)

@Serializable
data class PresetCall(
    val name: String,
    val preset: String,
)

@Serializable
data class DLibrary(
    var name: String,
    var presets: List<LibraryPreset>,
)
