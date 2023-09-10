---
layout: default
title: Todo
nav_order: 99
---

## Todo

使用场景，基于知识库的：

- 一句话生成前端页面 (特定框架与场景)
    1. Problem Analyser: 确认需求
        - [ ] 匹配现有需求
    2. Problem Clarifier: 确认输入和输出
        - [x] 输出 ASCII DSL 作为输入 （ [Design](https://github.com/phodal/design) ）
        - [x] 将历史页面转换为 ASCII DSL
    3. Solution Design: 设计布局
        - [ ] 搜索历史组件布局
        - [x] 匹配组件信息
    4. Solution Execute: 生成页面 (React, React Runtime)
        - [ ] Code Interpreter by [Unit Runtime](https://github.com/unit-mesh/unit-runtime)
- 一句话生成后端 API (Ktor, Spring, Kotless）
    1. 确认需求
    2. 输入和输出
    3. 确认生成的 API
- 一句话生成 SQL 图表
    1. 识别问题领域
    2. 确认与澄清问题
    3. 分析问题与数据
    4. 生成 SQL 与图表 (Kotlin Jupyter, [Lets-Plot](https://github.com/JetBrains/lets-plot-kotlin))
- 一句话生成测试用例数据
    1. 识别需求（bug、代码变更、用例）
        - [ ] 匹配搜索相关的代码、过往的用例
        - [ ] 建议的测试用例方法和示例
    2. 基于测试原则生成初步测试用例
    3. 基于规则检查测试用例，是否需要基于
    4. 生成结果（测试数据集）
