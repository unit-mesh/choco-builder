package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PromptScriptTest {
    @Test
    fun should_convert_from_string() {
        val example = """
name: "Open AI Verifier"
description: "Verify Open AI's LLM"

jobs:
  prompt-evaluate: # job name should be unique for each job
    description: "Evaluate prompt with different parameters"
    connection: connections.yml
    template: code-complete.open-ai.vm # auto choice template by extension
    template-datasource:
      - type: file
        value: testdata/sample.json

    strategy:
      - type: connection
        value:
          - type: range
            key: temperature
            range: 0.0~1.0
            step: 0.1

    validate: # optional
      - type: json-path
        value: ${'$'}.output.id
      - type: string
        value: output.length < 100
"""
        val promptScript = PromptScript.fromString(example)

        // then
        assertNotNull(promptScript)
        assertEquals("Open AI Verifier", promptScript!!.name)
        assertEquals("Verify Open AI's LLM", promptScript.description)
        assertEquals(1, promptScript.jobs.size)
        assertEquals("Evaluate prompt with different parameters", promptScript.jobs["prompt-evaluate"]!!.description)
    }

    @Test
    fun should_load_from_file() {
        val input = this.javaClass.getResource("/prompt.unit-mesh.yml")!!.readText()
        val promptScript = PromptScript.fromString(input)

        assertNotNull(promptScript)
    }

    @Test
    fun should_support_for_unit_eval() {
        val string = """name: "Eval Units"
description: "A sample of unit mesh"

jobs:
  prompt-evaluate: # job name should be unique for each job
    description: "Evaluate prompt with different parameters"
    connection: mock-connection.yml
    template: fixtures/code-completion.vm
    template-datasource:
      - type: file
        value: fixtures/code-completion.jsonl

    strategy:
      - type: datasource-collection
        value:
          - temperature: 0.3
            max_tokens: 1000

"""

        val promptScript = PromptScript.fromString(string)
        assertNotNull(promptScript)
    }
}
