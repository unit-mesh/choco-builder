# Prompt Script 

PromptScript主要用于评估prompt的效果，而不是用于多种不同的自然语言处理任务。 其作用包括：

- Prompt设计：PromptScript 可以帮助您设计不同类型的提示，以测试模型在各种任务上的性能。
- 评估参数：您可以使用 PromptScript 来探索不同的参数设置，如温度、最大生成长度等，以评估其对生成文本的影响。
- 生成大量示例：PromptScript 可以生成大量的示例提示，以用于评估模型的一致性和稳定性。
- 自动化评估：通过脚本化的方式，您可以轻松地对多个提示进行自动化评估，以便进行模型性能的比较和分析。

## Usecases

- 批量的 Prompt 生成，用于数据蒸馏。
- 测试 Prompt 有效性占比。诸如于不同参数的接受度，如 temperature、top-k、top-p、nucleus 等。
- 多角色互动。游戏剧情生成，模拟多个虚拟作者或角色之间的合作，以生成有趣的对话、故事或合作创作。

## Design

### Flow Format

1. file name with type in middle, like `prompt.<type>.vm`
2. prompt config for connection, like `prompt-connection.yaml`

### Template with data loader

1. try auto load from dataframe, and feed to template

