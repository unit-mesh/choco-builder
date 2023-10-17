
aritcles:

[团队的Code Review实践](https://www.thoughtworks.com/zh-cn/insights/blog/agile-engineering-practices/how-to-code-review](https://www.thoughtworks.com/zh-cn/insights/blog/agile-engineering-practices/how-to-code-review)

- 互相学习，知识共享
- 统一风格，提高代码质量
- 尽早暴露问题，降低修复成本

工具：[https://github.com/MTWGA/thoughtworks-code-review-tools](https://github.com/MTWGA/thoughtworks-code-review-tools)


## Paper

### Expectations, outcomes, and challenges of modern code review

从我们的研究中，我们为开发人员提出以下建议：

- 质量保证：代码审查的期望与实际结果存在不匹配。根据我们的研究，审查通常不如项目成员期望的那样频繁地发现缺陷，尤其是深层次、微妙或“宏观”层面的问题。以这种方式依赖代码审查来确保质量可能会面临风险。
- 理解：当审阅者在上下文和代码方面具有先验知识时，他们能够更快地完成审查并向作者提供更有价值的反馈。团队应该努力提高开发人员的广泛理解（如果更改的作者是唯一的专家，她就没有潜在的审阅者），在使用审查来发现缺陷时，更改的作者应该尽可能包括代码所有者和其他理解代码的人。开发人员表示，当作者在审查中向他们提供上下文和指导时，他们可以更好、更快地做出反应。
- 超越缺陷：现代代码审查提供了除了发现缺陷以外的好处。代码审查可以用于改善代码风格，寻找替代解决方案，增加学习，分享代码所有权等。这应该指导代码审查政策。
- 沟通：尽管有支持代码审查的工具不断增长，开发人员在审查时仍需要比注释更丰富的沟通方式。团队应该提供面对面或至少同步沟通的机制。

### Modern code review: a case study at google

At Google, over 35% of the changes under consideration modify only a single file and about 90% modify fewer than 10 files.
Over 10% of changes modify only a single line of code, and the median number of lines modified is 24. The median change size
is significantly lower than reported by Rigby and Bird for companies such as AMD (44 lines), Lucent (263 lines),
and Bing, Office and SQL Server at Microsoft (somewhere between those boundaries),
but in line for change sizes in open source projects.

在谷歌，有超过35%的正在考虑的更改仅修改一个文件，约90%的更改修改不到10个文件。
超过10%的更改仅修改一行代码，中位数修改的行数为24。中位数的更改大小明显低于Rigby和Bird为像AMD（44行）、Lucent（263行）以及微软的Bing、
Office和SQL Server（在这些边界之间）等公司报告的更改大小，但与开源项目的更改大小保持一致。

### Pre Review

简介一下：业务上下文

