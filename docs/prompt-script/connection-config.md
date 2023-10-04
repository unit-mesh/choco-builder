---
layout: default
title: ConnectionConfig
parent: Prompt Script
nav_order: 14
permalink: /prompt-script/connection-config
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# ConnectionConfig 

> A connection entity that stores the connection information default use `connection.yml` in current directory.

For example:
```yaml
name: open_ai_connection
type: OpenAI
secrets:
  api-key: ak-xxxx
```

current supported connection type:

- OpenAI
- CustomLlm

## MockLlmConnection 

MockLlmConnection is a mock connection for testing in local.
For example:
```yaml
name: mock_response
type: MockLlm

configs:
  api-response: "{\"text\": \"this is a mock resource\"}"
```

## OpenAiConnection 

OpenAiConnection is a connection for OpenAI API.
For example:
```yaml
name: open_ai_connection
type: OpenAI
secrets:
  api-key: ak-xxxx
```

If you are using proxyed OpenAI API, you can set the api-host in configs.

```yaml
name: open_ai_connection
type: OpenAI
configs:
  api-host: https://api.aios.chat/
secrets:
  api-key: ak-xxxx
```

