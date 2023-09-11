---
layout: default
title: Development Setup
nav_order: 2
permalink: /setup
---

# Development

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


