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
    label: 'Ktor',
    value: 'ktor'
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
