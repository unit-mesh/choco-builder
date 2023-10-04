---
layout: default
title: Validator
parent: Prompt Script
nav_order: 15
permalink: /prompt-script/validator
---

{: .warning }
Automatically generated documentation; use the command './gradlew :docs-builder:run' and update comments in the source code to reflect changes.

## Validator 

> Validator is an interface for validating result.

- ExtToolValidator. Extension Tool is validate command, like `plantuml xxx.puml`
- JsonPathValidator. JsonPath will validate is path is valid. If a path is invalid, will return false.
- JsonValidator. JsonValidator will validate is input is valid json. If input is invalid, will return false.
- RegexValidator. RegexValidator will validate is input matches regex. If input is invalid, will return false.
- StringValidator. This class represents a string validation expression that evaluates to a boolean value, determining whether subsequent
statements should be executed or not.


