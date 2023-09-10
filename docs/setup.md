---
layout: default
title: Development Setup
nav_order: 3
description: "环境搭建 | Chocolate Factory"
permalink: /
---

# Development

## 环境搭建

1. clone 代码

```bash
git clone https://github.com/unit-mesh/chocolate-factory
```

2. 配置 OpenAI API

```bash
export OPENAI_API_KEY=
# 当你使用的是非官方的 OpenAI 代理时，需要设置 OPENAI_API_HOST
export OPENAI_API_HOST=
```

3. 执行 bootRun

```bash
./gradlew bootRun
```

