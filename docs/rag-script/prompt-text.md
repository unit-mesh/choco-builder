---
layout: default
title: PromptText
parent: RAG Script
nav_order: 10
permalink: /rag-script/prompt-text
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# PromptText 

> PromptText is a DSL for writing a prompt, will generate Markdown text.
- codeblock, will generate a codeblock starts with ```language
- paragraph, will generate a paragraph
- list, will generate a list
- linebreak, will generate a linebreak

Sample: PromptScript

```kotlin
prompt {
    paragraph("Hello World")
    codeblock("kotlin") {
        "println(\"Hello World\")"
    }
    list(ListType.Unordered) {
        listOf("Hello", "World")
    }
}
```

## list 

type can be unordered, ordered, or checked

