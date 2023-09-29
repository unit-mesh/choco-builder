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
    template: code-complete.open-ai.vm # auto choice template by extension
    connection: # default values for all jobs
      file: connections.yml
      vars:
        - type: range
          key: temperature
          range: 0.0~1.0, 0.1
          step: 0.1

    vars: # some file or map
      name: "Phodal Huang"

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
}
