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
        title: '商品订单详细',
        content:
          '编写一个商品订单详细，布局方式：响应式，其中左边是一个商品过滤条件，右边则通过分页方式展示各种商品'
      },
      {
        title: '创建博客',
        content: '编写一个响应式布局的博客发表页，需要包含标题、内容、时间、存为草稿，如果创建成功则弹出对话框，显示：“创建成功!”'
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
    label: '逻辑分析 (Todo)',
    value: 'logic-analysis'
  },
  {
    label: '需求分析 (Todo)',
    value: 'requirement-analysis'
  },
  {
    label: 'Custom (Todo)',
    value: 'custom'
  }
]
