---
layout: default
title: Development Setup
nav_order: 98
permalink: /setup
---

# Development

## 技术栈

Tech Stack:

- [Spring Boot](https://spring.io/projects/spring-boot) is a framework for building web applications.
- [Kotlin](https://kotlinlang.org/) is a modern programming language that makes developers happier.
- [Kotlin Jupyter](https://github.com/Kotlin/kotlin-jupyter)  Kotlin kernel for Jupyter/IPython.
- [Kotlin Dataframe](https://github.com/Kotlin/dataframe) is typesafe in-memory structured data processing for JVM.

Vector Store:

- ElasticSearch is a distributed, RESTful search and analytics engine.

## 项目结构

- cocoa-core, 核心模块，包含了核心的流程控制、领域模型、领域流程等
- cocoa-local-embedding，本地向量化模块，包含了本地向量化的实现，如 Sentence-Transformers 等。
- code-interpreter，代码解释器，包含了代码解释器的实现，如 Kotlin Jupyter 等。
- dsl
    - design，Design DSL 的解析器
- rag-modules Retrieval Augmented Generation 相关模块
    - code-splitter，代码分割器，用于将代码分割成更小的单元
    - mivlus，基于 Mivlus 的检索器
    - pinecone，基于 Pinecone 的检索器

## 环境搭建

1.clone 代码

```bash
git clone https://github.com/unit-mesh/chocolate-factory
```

2.配置 OpenAI API

```bash
export OPENAI_API_KEY=
# 当你使用的是非官方的 OpenAI 代理时，需要设置 OPENAI_API_HOST
export OPENAI_API_HOST=
```

3.启动后端

```bash
./gradlew bootRun
```

4.启动前端

```bash
pnpm install
pnpm run dev
```

## CI/CD

采用的是 GitHub Actions，配置文件在 `.github/workflows` 目录下。
