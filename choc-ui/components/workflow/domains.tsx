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
    label: 'Code Interpreter (Kotlin)',
    value: 'code-interpreter'
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
