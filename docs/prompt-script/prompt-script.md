---
layout: default
title: Prompt Script
nav_order: 90
has_children: true
permalink: /prompt-script
---

PromptScript 是一个轻量级的 Prompt 调试用的 DSL，以用于快速使用、构建 Prompt。

1. 下载 CLI。从 [releases](https://github.com/unit-mesh/chocolate-factory/releases) 下载 prompt-script-all.jar，（这里的 * 是版本号）。
2. 配置 PromptScript
3. 运行 PromptScript

示例见：[examples/promptscript](https://github.com/unit-mesh/chocolate-factory/tree/master/examples/promptscript)

```bash
java -jar prompt-script-0.3.5-all.jar --input examples/promptscript/prompt.unit-mesh.yml

execute job: prompt-evaluate
connection file: /Volumes/source/ai/chocolate-factory/examples/promptscript/openai-connection.yml
15:00:04.450 [main] DEBUG org.apache.velocity - Initializing Velocity, Calling init()...
15:00:04.453 [main] DEBUG org.apache.velocity - Starting Apache Velocity v2.3
15:00:04.457 [main] DEBUG org.apache.velocity - Default Properties resource: org/apache/velocity/runtime/defaults/velocity.properties
15:00:04.470 [main] DEBUG org.apache.velocity - ResourceLoader instantiated: org.apache.velocity.runtime.resource.loader.FileResourceLoader
15:00:04.470 [main] DEBUG org.apache.velocity.loader.file - FileResourceLoader: adding path '.'
15:00:04.471 [main] DEBUG org.apache.velocity - initialized (class org.apache.velocity.runtime.resource.ResourceCacheImpl) with class java.util.Collections$SynchronizedMap cache map.
15:00:04.473 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Stop
15:00:04.474 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Define
15:00:04.474 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Break
15:00:04.474 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Evaluate
15:00:04.475 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Macro
15:00:04.476 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Parse
15:00:04.476 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Include
15:00:04.477 [main] DEBUG org.apache.velocity - Loaded System Directive: org.apache.velocity.runtime.directive.Foreach
15:00:04.494 [main] DEBUG org.apache.velocity.parser - Created '20' parsers.
15:00:04.513 [main] DEBUG org.apache.velocity.macro - "velocimacro.library.path" is not set. Trying default library: velocimacros.vtl
15:00:04.514 [main] DEBUG org.apache.velocity.macro - Default library velocimacros.vtl not found. Trying old default library: VM_global_library.vm
15:00:04.514 [main] DEBUG org.apache.velocity.macro - Old default library VM_global_library.vm not found.
15:00:04.514 [main] DEBUG org.apache.velocity.macro - allowInline = true: VMs can be defined inline in templates
15:00:04.514 [main] DEBUG org.apache.velocity.macro - allowInlineToOverride = false: VMs defined inline may NOT replace previous VM definitions
15:00:04.514 [main] DEBUG org.apache.velocity.macro - allowInlineLocal = false: VMs defined inline will be global in scope if allowed.
15:00:04.514 [main] DEBUG org.apache.velocity.macro - autoload off: VM system will not automatically reload global library macros
15:00:13.003 [main] DEBUG com.jayway.jsonpath.internal.path.CompiledPath - Evaluating path: $['id']
15:00:13.009 [main] ERROR cc.unitmesh.prompt.executor.ScriptExecutor - class cc.unitmesh.prompt.validate.JsonPathValidator validate failed: {"text":"目前最流行的前端框架有React、Angular和Vue.js"}
15:00:13.010 [main] ERROR cc.unitmesh.prompt.executor.ScriptExecutor - class cc.unitmesh.prompt.validate.StringValidator validate failed: {"text":"目前最流行的前端框架有React、Angular和Vue.js"}
```

