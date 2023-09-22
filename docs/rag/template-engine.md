---
layout: default
title: Template Engine
parent: Retrieval Augmented Generation
nav_order: 99
---

## [PromptFlow](https://github.com/microsoft/promptflow)

PromptFlow use [Jinja2](https://jinja.palletsprojects.com/en/3.0.x/) as template engine.

```jinja2
system:
You are a helpful assistant.

{% for item in chat_history %}
user:
{{item.inputs.question}}
assistant:
{{item.outputs.answer}}
{% endfor %}

user:
{{question}}
````
