# Chocolate Factory

> Chocolate Factory 是一款开源的 LLM 应用引擎，旨在帮助您打造 LLM 生成助手。

Tech Stack:

- [Kotlin Dataframe](https://github.com/Kotlin/dataframe)  typesafe in-memory structured data processing for JVM.
- [KInference](https://github.com/JetBrains-Research/kinference) is a library that makes it possible to execute complex
  ML models (written via ONNX) in Kotlin.

使用场景，基于知识库：

- 一句话生成前端页面 (特定框架与场景)
    1. 确认需求
    2. 确认布局
    3. 生成页面 (React, React Runtime)
- 一句话生成后端 API (Ktor, Spring, Kotless）
    1. 确认需求与输入和输出
    2. 确认数据库表
    3. 确认 API
- 一句话生成 SQL 图表
    1. 识别问题领域
    2. 确认与澄清问题
    3. 分析问题与数据
    4. 生成 SQL 与图表 (Kotlin Jupyter, [Lets-Plot](https://github.com/JetBrains/lets-plot-kotlin))
- 一句话生成 API

## License

This code is distributed under the MPL 2.0 license. See `LICENSE` in this directory.
