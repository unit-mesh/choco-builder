---
layout: default
title: Response Validate
parent: Prompt Script
nav_order: 3
---

```yaml
validate: # optional
  - type: json-path
    value: $.id
  - type: string
    value: output.length > 300
```

