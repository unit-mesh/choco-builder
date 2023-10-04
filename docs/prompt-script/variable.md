---
layout: default
title: Variable
parent: Prompt Script
nav_order: 13
permalink: /prompt-script/variable
---

{: .warning }
Automatically generated documentation; use the command './gradlew :docs-builder:run' and update comments in the source code to reflect changes.

## Variable 

> Variable can be used for [cc.unitmesh.prompt.model.JobStrategy.Connection]

- KeyValue. A key-value variable, e.g.: `{"key": "value"}`
- Range. A range string, could be:
1. use `~` as format, e.g.: 0.0~1.0, 0~100, 0~1000, 0.0~100.0, 0.0~1000.0
2. use `..` as format, e.g.: 0.0..1.0, 0..100, 0..1000, 0.0..100.0, 0.0..1000.0
3. use `:` as format, e.g.: 0.0:1.0, 0:100, 0:1000, 0.0:100.0, 0.0:1000.0
4. use `to` as format, e.g.: 0.0 to 1.0, 0 to 100, 0 to 1000, 0.0 to 100.0, 0.0 to 1000.0

So, we will parse [step] to confirm is int or float, then parse [range] to get [ClosedRange].

