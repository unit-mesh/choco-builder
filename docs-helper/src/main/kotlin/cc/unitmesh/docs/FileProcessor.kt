package cc.unitmesh.docs

import cc.unitmesh.docs.base.Code
import cc.unitmesh.docs.base.DEFAULT_PATTERNS
import cc.unitmesh.docs.base.fileSequence
import com.intellij.mock.MockProject
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import java.nio.file.*
import kotlin.io.path.pathString

class FileProcessor {
    private var psiFileFactory: PsiFileFactory

    init {
        psiFileFactory = createPsiFactory()!!
    }

    private fun createPsiFactory(): PsiFileFactory? {
        val compilerConfiguration = CompilerConfiguration()
        compilerConfiguration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        val disposable = Disposer.newDisposable()
        try {
            val project =
                KotlinCoreEnvironment
                    .createForProduction(
                        disposable,
                        compilerConfiguration,
                        EnvironmentConfigFiles.JVM_CONFIG_FILES,
                    ).project as MockProject

            return PsiFileFactory.getInstance(project)
        } finally {
            // Dispose explicitly to (possibly) prevent memory leak
            // https://discuss.kotlinlang.org/t/memory-leak-in-kotlincoreenvironment-and-kotlintojvmbytecodecompiler/21950
            // https://youtrack.jetbrains.com/issue/KT-47044
            disposable.dispose()
        }
    }

    fun process(rootDir: Path): List<KtFile> {
        return FileSystems
            .getDefault()
            .fileSequence(DEFAULT_PATTERNS, rootDir)
            .map { it.toFile() }
            .map { file ->
                process(code = Code.fromFile(file))
            }.toList()
    }

    private fun process(code: Code): KtFile {
        val psiFileName =
            code
                .filePath
                ?.pathString
                ?: if (code.script) {
                    "File.kts"
                } else {
                    "File.kt"
                }

        val psiFile =
            psiFileFactory.createFileFromText(
                psiFileName,
                KotlinLanguage.INSTANCE,
                code.content,
            ) as KtFile
        return psiFile
    }
}
