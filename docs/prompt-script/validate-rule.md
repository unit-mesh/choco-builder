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

JsonPath represents a JSON path expression that extracts values from a job's result.
This class is implemented with validation by [cc.unitmesh.prompt.validate.JsonPathValidator].
It relies on the JsonPath library available at [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath).
Usage example:

```yaml
- type: json-path
  value: $.store.book[0].title
```

## StringRule 

String represents a string validation expression that evaluates to a boolean value,
determining whether subsequent statements should be executed or not.
This class is implemented with validation by [cc.unitmesh.prompt.validate.StringValidator].
Usage example:

```yaml
- type: string
  value: output contains "hello"
```
Other expression:
- output contains "hello"
- output endsWith "world"
- output startsWith "hello"
- output == "hello world"
- output == 'hello world'
- output.length > 5"

## Regex 

Regex Represents a validation rule for using regular expressions to validate a job's result.
This class is implemented with validation by [cc.unitmesh.prompt.validate.RegexValidator].
Usage example:

```yaml
- type: regex
  value: \d{4}
```

## MarkdownCodeBlock 

MarkdownCodeBlock Represents a validation rule for verifying if a job's result is a valid Markdown code block.
This class is implemented with validation by [cc.unitmesh.prompt.validate.MarkdownCodeBlockValidator].
Usage example:

```yaml
- type: markdown-code
```

## Json 

Represents a validation rule for verifying if a job's result is valid JSON.
This class is implemented with validation by [cc.unitmesh.prompt.validate.JsonValidator].
Usage example:

```yaml
- type: json
```

## ExtTool 

Represents a validation rule for using an external tool to validate a job's result, such as PlantUML or Graphviz.
This class is implemented with validation by [cc.unitmesh.prompt.validate.ExtToolValidator].
Usage example:

```yaml
- type: ext-tool
  value: dot -Tsvg basic.dot -o basic.svg
```

