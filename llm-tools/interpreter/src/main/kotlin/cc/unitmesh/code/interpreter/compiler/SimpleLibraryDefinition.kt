package cc.unitmesh.code.interpreter.compiler

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.kotlinx.jupyter.api.AfterCellExecutionCallback
import org.jetbrains.kotlinx.jupyter.api.ClassAnnotationHandler
import org.jetbrains.kotlinx.jupyter.api.CodePreprocessor
import org.jetbrains.kotlinx.jupyter.api.ExecutionCallback
import org.jetbrains.kotlinx.jupyter.api.FieldHandler
import org.jetbrains.kotlinx.jupyter.api.FileAnnotationHandler
import org.jetbrains.kotlinx.jupyter.api.InternalVariablesMarker
import org.jetbrains.kotlinx.jupyter.api.InterruptionCallback
import org.jetbrains.kotlinx.jupyter.api.KotlinKernelVersion
import org.jetbrains.kotlinx.jupyter.api.RendererHandler
import org.jetbrains.kotlinx.jupyter.api.TextRendererWithPriority
import org.jetbrains.kotlinx.jupyter.api.ThrowableRenderer
import org.jetbrains.kotlinx.jupyter.api.libraries.ColorSchemeChangedCallback
import org.jetbrains.kotlinx.jupyter.api.libraries.KernelRepository
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryDefinition
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryResource
import org.jetbrains.kotlinx.jupyter.util.AcceptanceRule

@Serializable
class SimpleLibraryDefinition(
    override var imports: List<String> = emptyList(),
    override var dependencies: List<String> = emptyList(),
    override var repositories: List<KernelRepository> = emptyList()
) : LibraryDefinition {
    @Transient
    override var options: Map<String, String> = emptyMap()

    @Transient
    override var init: List<ExecutionCallback<*>> = emptyList()

    @Transient
    override var initCell: List<ExecutionCallback<*>> = emptyList()

    @Transient
    override var afterCellExecution: List<AfterCellExecutionCallback> = emptyList()

    @Transient
    override var shutdown: List<ExecutionCallback<*>> = emptyList()

    @Transient
    override var renderers: List<RendererHandler> = emptyList()

    @Transient
    override var textRenderers: List<TextRendererWithPriority> = emptyList()

    @Transient
    override var throwableRenderers: List<ThrowableRenderer> = emptyList()

    @Transient
    override var converters: List<FieldHandler> = emptyList()

    @Transient
    override var classAnnotations: List<ClassAnnotationHandler> = emptyList()

    @Transient
    override var fileAnnotations: List<FileAnnotationHandler> = emptyList()

    @Transient
    override var resources: List<LibraryResource> = emptyList()

    @Transient
    override var codePreprocessors: List<CodePreprocessor> = emptyList()

    @Transient
    override var internalVariablesMarkers: List<InternalVariablesMarker> = emptyList()

    @Transient
    override var minKernelVersion: KotlinKernelVersion? = null

    @Transient
    override var originalDescriptorText: String? = null

    @Transient
    override var integrationTypeNameRules: List<AcceptanceRule<String>> = emptyList()

    @Transient
    override var interruptionCallbacks: List<InterruptionCallback> = emptyList()

    @Transient
    override var colorSchemeChangedCallbacks: List<ColorSchemeChangedCallback> = emptyList()

}