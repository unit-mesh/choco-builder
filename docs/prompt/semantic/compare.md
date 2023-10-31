---
layout: default
title: Semantic Compare
parent: Prompt Sample
nav_order: 35
---

## GitHub Copilot

## Intellij Prompts

The function presented serves as a comprehensive search utility within JetBrains IDEs,
It behaves like exact_search.
The only difference is that this function allows not exact queries, as search is embedding-based in this case
Use this function, IF YOU DO NOT KNOW THE **EXACT** NAME the named entity you are looking for OR
if exact_search failed.

```json
{
  "type": "object",
  "properties": {
    "searchType": {
      "type": "string",
      "enum": [
        "actions",
        "symbols",
        "classes",
        "files"
      ]
    },
    "query": {
      "type": "string",
      "description": "Query in textual form for searching by entity names."
    }
  },
  "required": [
    "searchType",
    "query"
  ]
}
```
