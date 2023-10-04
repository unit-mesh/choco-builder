---
layout: default
title: ValidateRule
parent: Prompt Script
nav_order: 12
permalink: /prompt-script/validate-rule
---

{: .warning }
Automatically generated documentation; use the command './gradlew :docs-builder:run' and update comments in the source code to reflect changes.

## ValidateRule 

> ValidateRule will be used to validate the job's result.

- JsonPath. JsonPath is a json path expression, which will be used to extract the value from the job's result.
- StringRule. String will use string expression, like the [String.contains] method to check the job's result.
- Regex. Regex will use regex expression to check the job's result.
- MarkdownCodeBlock. MarkdownCodeBlock will verify the job's result is a valid markdown code block.
- Json. Json will check the job's result is a valid json.
- ExtTool. ExtTool will use the external tool to check the job's result, like PlantUML, Graphviz, etc.
