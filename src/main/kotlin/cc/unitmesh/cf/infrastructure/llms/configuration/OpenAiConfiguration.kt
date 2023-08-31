package cc.unitmesh.cf.infrastructure.llms.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
class OpenAiConfiguration {
    lateinit var apiKey: String
    var serverAddress: String? = null
}
