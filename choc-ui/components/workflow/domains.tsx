export type DomainFormItem = {
  label: string
  value: string
}

export const domains: DomainFormItem[] = [
  {
    label: 'Frontend',
    value: 'frontend'
  },
  {
    label: 'OpenAPI',
    value: 'openapi'
  },
  {
    label: '后端（Spring 框架）',
    value: 'spring'
  },
  {
    label: 'TestCase',
    value: 'testcase'
  },
  {
    label: 'SQL',
    value: 'sql'
  },
  {
    label: 'Custom',
    value: 'custom'
  }
]
