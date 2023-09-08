export type DomainFormItem = {
  label: string
  value: string
  examples?: {
    title: string
    content: string
  }[]
}

export const domains: DomainFormItem[] = [
  {
    label: 'Frontend',
    value: 'frontend',
    examples: [
      {
        title: '生成聊天页',
        content:
          '编写一个聊天页，布局方式：响应式，其中左边是一个聊天列表页，中间是聊天区（使用卡片），聊天区的下方是输入区。'
      }
    ]
  },
  {
    label: 'OpenAPI (Todo)',
    value: 'openapi'
  },
  {
    label: 'Code Interpreter (Kotlin)',
    value: 'code-interpreter',
    examples: [
      {
        title: '生成图表',
        content:
          '生成一个 2023 年上半年电费图，信息如下：###1~6 月：201.2,222,234.3,120.2,90,90.4###'
      },
      {
        title: '编写和执行代码',
        content: '编写乘法表'
      }
    ]
  },
  {
    label: 'TestCase（测试用例）',
    value: 'testcase',
    examples: [
      {
        title: '生成测试用例',
        content: '在京西商城中，实现商品管理的上架商品功能'
      }
    ]
  },
  {
    label: 'SQL (Todo)',
    value: 'sql'
  },
  {
    label: 'Custom (Todo)',
    value: 'custom'
  }
]
