package cc.unitmesh.prompt.config;

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class ConfigurationTest {
    @Test
    fun should_convert_connection() {
        val example = """
type: openai # like azure-openai, bard, llama, etc.
vars:
  model: gpt-3.5-turbo
  temperature: 0.0~1.0, 0.1
"""

        val connection: Connection = Yaml.default.decodeFromString(example)

        // then
        assertNotNull(connection)
        assertEquals("gpt-3.5-turbo", connection.vars["model"]!!)
    }

    @Test
    fun should_convert_from_string() {
        val example = """
name: "Open AI Verifier"
description: "Verify Open AI's LLM"

jobs:
  prompt-evaluate: # job name should be unique for each job
    description: "Evaluate prompt with different parameters"
    template: code-complete.open-ai.vm # auto choice template by extension
    connection: # default values for all jobs
      file: connections.yml
      vars:
        temperature: 0.0~1.0, 0.1

    vars: # some file or map
      name: "Phodal Huang"

    validate: # optional
      - type: json-path
        value: ${'$'}.output.id
      - type: string
        value: output.length < 100
"""
        val configuration = Configuration.fromString(example)

        // then
        assertNotNull(configuration)
        assertEquals("Open AI Verifier", configuration!!.name)
        assertEquals("Verify Open AI's LLM", configuration.description)
        assertEquals(1, configuration.jobs.size)
        assertEquals("Evaluate prompt with different parameters", configuration.jobs["prompt-evaluate"]!!.description)
    }
}
