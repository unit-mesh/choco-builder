---
layout: default
title: Design DSL
parent: Evaluation
nav_order: 1
permalink: /evaluation/github-copilot
---

介绍：[Research: quantifying GitHub Copilot’s impact on developer productivity and happiness](https://github.blog/2022-09-07-research-quantifying-github-copilots-impact-on-developer-productivity-and-happiness/)

框架：[SPACE of Developer Productivity](https://queue.acm.org/detail.cfm?id=3454124)

方法论：

![Space Framework](https://dl.acm.org/cms/attachment/9f3f11c8-8c6e-48e9-a1a2-7772a8cfa441/forsgren1.png)

## Space Framework

- Satisfaction and
  well-being，满意度和幸福感。满意度是开发者对他们的工作、团队、工具或文化感到满足的程度；幸福感是他们的健康和快乐程度，以及他们的工作对此的影响。衡量满意度和幸福感有助于理解生产力，甚至可能预测生产力。
- Performance，性能。性能是系统或过程的结果。衡量软件开发人员的性能很难量化，因为很难直接将个体贡献与产品结果联系起来。
- Activity，活动。活动是在执行工作过程中完成的动作或产出的计数。如果正确测量，开发者活动可以为理解开发者生产力、工程系统和团队效率提供有价值但有限的见解。
- Communication and
  collaboration，沟通和协作。沟通和协作捕捉人们和团队如何进行沟通和共同工作。软件开发是一个依赖于团队内外广泛而有效的沟通、协调和协作的协作和创造性任务。成功为对方工作并高效地整合对方工作的有效团队依赖于对团队成员活动和任务优先级的高度透明性和意识。
- Efficiency and flow，效率和流程。效率和流程捕捉在个体或通过系统完成工作或取得进展的能力，是否有最小的中断或延迟。这可以包括团队内外活动的协调程度以及是否正在不断取得进展。  

### Metrics

SPACE框架指标的观察到的变化：

满意度和幸福感

- 所有团队成员在编码过程中报告更少的沮丧感
- 团队成员发现他们的任务更加令人满足
- 由于Copilot帮助处理重复性任务，每个人都能够专注于更令人满意的工作性能
- 项目交付时间从预期的 22 天减少到 10 天（预期时间的 45%）
- 在内部测试期间，每千行代码的缺陷数量为2.63（行业平均值在10到20个缺陷/千行代码之间）

活动
- 每天的代码行数：总体上比预期基线增加了 +80% 的行数
- 完全记录的方法/类：覆盖率从 35% 增加到 90%，增长了 +157%
- 每天的 Pull Requests：节奏从每天 1 个增加到 2.2 个，增长了 +120%

沟通和协作
- 由于 Copilot 帮助缩小驾驶员和导航员之间的经验差距，配对编程比平常更有效
- 由于更好的类/方法文档和改进的代码质量，代码审查更快，评论者要求的更改更少

效率和流程
- 所有团队成员报告在完成任务方面更快，特别是重复性任务
- 团队成员还报告在流动状态中花费的时间显着增加（>50%）

其他观察：

- 与GitHub Next 研究团队（A. Ziegler等人，2022）一样，我们发现接受率（即用户接受的显示完成的百分比）是团队对生产力感知的最佳预测因子。
- 主要问题是不准确的建议，但随着代码库的增长，它们变得更少。
- 重构和正确注释代码的速度显着加快。
- 那些对 Python 经验较少的人也观察到，Copilot 让他们能够应用他们的软件工程技能，否则他们将无法做到。这帮助他们比预期更快地学习 “Pythonic方式”做事。
