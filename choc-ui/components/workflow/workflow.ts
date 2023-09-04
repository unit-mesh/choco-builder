import { Stage } from '@/components/workflow/stage'

enum ExampleType {
  NONE,
  ONE_CHAT,
  MULTI_CHAT
}

class PromptTemplate {
  id: string
  stage: Stage
  systemPrompt: string
  exampleType: ExampleType
  examples: QAExample[]
  updateExamples: QAUpdateExample[]

  constructor(
    stage: Stage,
    systemPrompt: string,
    exampleType: ExampleType = ExampleType.NONE,
    examples: QAExample[] = [],
    updateExamples: QAUpdateExample[] = []
  ) {
    this.id = ''
    this.stage = stage
    this.systemPrompt = systemPrompt
    this.exampleType = exampleType
    this.examples = examples
    this.updateExamples = updateExamples
  }
}

class Workflow {
  prompts: PromptTemplate[]

  constructor() {
    this.prompts = []
  }
}
