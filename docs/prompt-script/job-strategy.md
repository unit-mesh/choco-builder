---
layout: default
title: Job Strategy
parent: Prompt Script
nav_order: 2
---

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