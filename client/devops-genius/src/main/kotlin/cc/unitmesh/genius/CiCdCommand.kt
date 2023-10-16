package cc.unitmesh.genius

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.serializer
import org.changelog.CommitParser
import org.changelog.ParserOptions
import java.io.File

class CiCdCommand : CliktCommand(help = "Auto create CI/CD script with GenAI") {
    override fun run() {

    }
}
