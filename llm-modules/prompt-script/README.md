# Prompt Script

PromptScript主要用于测试和评估 Prompt 的效果，而不是用于多种不同的自然语言处理任务。 其作用包括：

- Prompt 设计：PromptScript 可以帮助您设计不同类型的提示，以测试模型在各种任务上的性能。
- Prompt 改进建议？：PromptScript 可以帮助您快速地尝试不同的提示
- 批量评估：您可以使用 PromptScript 来探索不同的参数设置，如温度、最大生成长度等，以评估其对生成文本的影响。
- 生成大量示例：PromptScript 可以生成大量的示例提示，以用于评估模型的一致性和稳定性。
- 自动化评估：通过脚本化的方式，您可以轻松地对多个提示进行自动化评估，以便进行模型性能的比较和分析。

## Usecases

- 批量的 Prompt 生成，用于数据蒸馏。
- 测试 Prompt 有效性占比。诸如于不同参数的接受度，如 temperature、top-k、top-p、nucleus 等。
- 多角色互动。游戏剧情生成，模拟多个虚拟作者或角色之间的合作，以生成有趣的对话、故事或合作创作。

## Design

### Flow File Format

1. file name with type in middle, like `<flow-name>.<type>.vm`
2. prompt config for connection, like `connections.yml`, for LLM/AI, Tool, Embedding, Database, etc.
3. flow config：`flow.script.yml`

### Prompt Parameter Range

for example:

RangeType: Int, Float, Boolean

1. temperature range: 0.0~1.0
2. repeat range: 1,000,000,000

### Assert Refs

[Promptfoo](https://github.com/promptfoo/promptfoo) can test your prompts. Evaluate and compare LLM outputs, catch
regressions, and improve prompt quality.

- 系统地测试预定义的测试用例中的提示和模型
- 通过将 LLM 输出进行对比来评估质量并捕捉回归问题
- 利用缓存和并发加速评估过程
- 通过定义测试用例自动评分输出
- 作为命令行工具、库或在CI/CD中使用

使用 OpenAI、Anthropic、Azure、Google、Llama 等开源模型，或集成自定义 API 提供者用于任何 LLM API。

```yaml
prompts: [ prompt1.txt, prompt2.txt ]
providers: [ openai:gpt-3.5-turbo, llama:llama2:70b ]
tests:
  - description: 'Test translation to French'
    vars:
      language: French
      input: Hello world
    assert:
      - type: contains-json
      - type: javascript
        value: output.length < 100

  - description: 'Test translation to German'
    vars:
      language: German
      input: How's it going?
    assert:
      - type: model-graded-closedqa
        value: does not describe self as an AI, model, or chatbot
      - type: similar
        value: was geht
        threshold: 0.6 # cosine similarity
```

## Our Version

```yaml
name: "Open AI Verifier"
description: "Verify Open AI's LLM"

jobs:
  prompt-evaluate: # job name
    description: "Evaluate prompt with different parameters"
    template: prompt-evaluate.vm # auto choice template by extension
    defaults:
      connection: openai:gpt-3.5-turbo # canonical name model
    connection-vars:
      temperature:
        type: float
        range: 0.0~1.0
        step: 0.1
    vars: # some file or map
      name: "Phodal Huang"

    assert: # optional
      - type: contains-json
      - type: javascript # json path ?
        value: output.length < 100
```
