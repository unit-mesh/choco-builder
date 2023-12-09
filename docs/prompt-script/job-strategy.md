---
layout: default
title: JobStrategy
parent: Prompt Script
nav_order: 10
permalink: /prompt-script/job-strategy
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# JobStrategy 

> JobStrategy is the job's strategy config, which will be used for [cc.unitmesh.prompt.model.Job].
The strategy can be a connection config or a repeat config or others.
For example:

```yaml
strategy:
   - type: connection
     value:
       - type: range
         key: temperature
         range: 0.7~1.0
         step: 0.1
   - type: repeat
     value: 3
```

## Connection 

Connection is a config of [cc.unitmesh.connection.ConnectionConfig],
which will be used for [cc.unitmesh.openai.LlmProvider]
like temperature, top-p, top-k, presence_penalty, frequency_penalty, stop etc.
for example:

```yaml
- type: connection
  value:
    - type: range
      key: temperature
      range: 0.7~1.0
      step: 0.1
```


## Repeat 

Repeat is a config of repeat times.
for example:

```yaml
- type: repeat
  value: 3
```

## DatasourceCollection 

Represents a collection of data sources.

```yaml
- type: datasource-collection
  value:
    - temperature: 0.3
      max_tokens: 1000
```

