---
layout: default
title: Resources
nav_order: 99
permalink: /resources
---

# 相关资源

### 标准化 OpenAI API 接口实现

[ialacol](https://github.com/chenhunghan/ialacol) 是一个 OpenAI API 的轻量级直接替代品。ialacol is inspired by other
similar projects
like [LocalAI](https://github.com/go-skynet/LocalAI), [privateGPT](https://github.com/imartinez/privateGPT), [local.ai](https://github.com/louisgv/local.ai), [llama-cpp-python](https://github.com/abetlen/llama-cpp-python), [closedai](https://github.com/closedai-project/closedai),
and [mlc-llm](https://github.com/mlc-ai/mlc-llm), with a specific focus on Kubernetes deployment.

[Features](https://github.com/chenhunghan/ialacol?tab=readme-ov-file#features)

*   Compatibility with OpenAI APIs, compatible with [langchain](https://github.com/hwchase17/langchain).
*   Lightweight, easy deployment on Kubernetes clusters with a 1-click Helm installation.
*   Streaming first! For better UX.
*   Optional CUDA acceleration.
*   Compatible with [Github Copilot VSCode Extension](https://marketplace.visualstudio.com/items?itemName=GitHub.copilot)

Supported Models

See Receipts below for instructions of deployments.

*   [LLaMa 2 variants](https://huggingface.co/meta-llama)
*   [OpenLLaMA variants](https://github.com/openlm-research/open_llama)
*   [StarCoder variants](https://huggingface.co/bigcode/starcoder)
*   [WizardCoder](https://huggingface.co/WizardLM/WizardCoder-15B-V1.0)
*   [StarChat variants](https://huggingface.co/HuggingFaceH4/starchat-beta)
*   [MPT-7B](https://www.mosaicml.com/blog/mpt-7b)
*   [MPT-30B](https://huggingface.co/mosaicml/mpt-30b)
*   [Falcon](https://falconllm.tii.ae/)

And all LLMs supported by [ctransformers](https://github.com/marella/ctransformers/tree/main/models/llms).
