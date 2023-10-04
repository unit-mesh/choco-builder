---
layout: default
title: ValidateRule
parent: Prompt Script
nav_order: 12
permalink: /prompt-script/validate-rule
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# ValidateRule 

> ValidateRule will be used to validate the job's result.
Currently, we support JsonPath, String, Regex, MarkdownCodeBlock, Json, ExtTool.
For example:

```yaml
validate:
  - type: json-path
    value: $.output.id
  - type: string
    value: output.length < 100
```


## JsonPath 

type: json-path, is a json path expression, which will be used to extract the value from the job's result.

## StringRule 

type: string, will use string expression, like the [String.contains] method to check the job's result.

## Regex 

type: regex, will use regex expression to check the job's result.

## MarkdownCodeBlock 

type: markdown-code, will verify the job's result is a valid markdown code block.

## Json 

type: json, will check the job's result is a valid json.

## ExtTool 

type: ext-tool, will use the external tool to check the job's result, like PlantUML, Graphviz, etc.

