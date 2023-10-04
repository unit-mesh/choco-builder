---
layout: default
title: Prompt Script
nav_order: 90
has_children: true
permalink: /prompt-script
---

PromptScript 是一个轻量级的 Prompt 调试用的 DSL，以用于快速使用、构建 Prompt。

1. 下载 CLI。从 [releases](https://github.com/unit-mesh/chocolate-factory/releases) 下载 prompt-script-all.jar，（这里的 * 是版本号）。
2. 配置 PromptScript 的 YAML 文件。
3. 运行 PromptScript

示例见：[examples/promptscript](https://github.com/unit-mesh/chocolate-factory/tree/master/examples/promptscript)

日志示例:

```bash
java -jar prompt-script-0.3.5-all.jar --input examples/promptscript/prompt.unit-mesh.yml

[CF] c.u.p.e.ScriptExecutor execute job: prompt-evaluate
[CF] c.u.p.e.ScriptExecutor connection file: /Volumes/source/ai/chocolate-factory/examples/promptscript/openai-connection.yml
[CF] c.j.j.i.p.CompiledPath Evaluating path: $['id']
[CF] c.u.p.e.ScriptExecutor JsonPathValidator validate failed: {"text":"目前最流行的前端框架有React、Angular和Vue.js"}
[CF] c.u.p.e.ScriptExecutor StringValidator validate failed: {"text":"目前最流行的前端框架有React、Angular和Vue.js"}
[CF] c.u.p.e.ScriptExecutor write result to file: examples/promptscript/prompt-evaluate-2023-10-01T12-34-42.269901.txt
[CF] c.u.p.PromptScriptCommand execute script success: examples/promptscript/prompt.unit-mesh.yml
```

## PromptScript 示例

```yml
name: "Open AI Verifier"
description: "Verify Open AI's LLM"

jobs:
  prompt-evaluate:
    description: "Evaluate prompt with different parameters"
    connection: connection.yml
    template: code-complete.open-ai.vm
    template-datasource:
      - type: file
        value: datasource.json

    strategy:
      - type: connection
        value:
          - type: range
            key: temperature
            range: 0.7~1.0
            step: 0.1

    validate: # optional
      - type: json-path
        value: $.id
      - type: string
        value: output.length > 300
```
