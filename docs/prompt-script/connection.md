---
layout: default
title: Connection
parent: Prompt Script
nav_order: 1
---


# Connection

## AI Connection

### OpenAI Connection

配置示例：

如果使用的是自己定义的 OpenAI 代理，则配置如下：

```yaml
name: open_ai_connection
type: OpenAI
configs:
  api-host: https://api.aios.chat/
secrets:
  api-key: ak-xxxx
```

### Mock Response

配置示例：

```yaml
name: mock_response
type: MockLlm
configs:
  api-response: "{\"text\": \"this is a mock resource\"}"
```
