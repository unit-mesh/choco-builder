---
layout: default
title: Semantic Compare
parent: Prompt Sample
nav_order: 35
---

Other References: [https://github.com/jujumilk3/leaked-system-prompts](https://github.com/jujumilk3/leaked-system-prompts)

## GitHub Copilot

Copilot is an AI programming assistant that resides within a chat window in a VS code IDE.

The user has the following query:

hello

Since this is a chat window, Copilot may need additional context to answer the user's question.

Copilot has access to the information within the VS Code IDE, and can request that information before responding to the user.
    
Which of the following information would be most relevant for Copilot to answer the user's question?
Limit the answer to one to three sources and provide them in order of highest to lowest priority as a comma-separated list without extra information. You must not come up with new sources. If none of the information is relevant, respond \"None\". End the list with a ;
    
List of possible information sources:
- current-editor:source code in the active document
- problems-in-active-document:warnings and errors in active document
- current-selection:Active selection
- git-metadata:Metadata about the current git repository
- debug-console-output:Debug console output
- terminal:The contents of the debug console
- corresponding-test-file:Corresponding test file
- vscode:relevant information about vs code commands and settings


Example Response:
current-editor,active-editor-filenames;

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
