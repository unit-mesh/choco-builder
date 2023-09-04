import { Stage } from '@/components/workflow/stage'
import {
  QAExample,
  QAUpdateExample
} from '@/components/workflow/prompt-example'

export enum ExampleType {
  NONE,
  ONE_CHAT,
  MULTI_CHAT
}

export class PromptWithStage {
  id: string
  stage: Stage
  systemPrompt: string
  exampleType: ExampleType
  examples: QAExample[]
  updateExamples: QAUpdateExample[]
  isDone: boolean = false

  constructor(
    stage: Stage,
    systemPrompt: string,
    exampleType: ExampleType = ExampleType.NONE,
    examples: QAExample[] = [],
    updateExamples: QAUpdateExample[] = [],
    isDone: boolean = false
  ) {
    this.id = ''
    this.stage = stage
    this.systemPrompt = systemPrompt
    this.exampleType = exampleType
    this.examples = examples
    this.updateExamples = updateExamples
    this.isDone = isDone
  }
}

export class Workflow {
  prompts: PromptWithStage[]

  constructor() {
    this.prompts = []
  }

  // iteration prompts and find the first prompt that is not done
  get currentPrompt() {
    return this.prompts.find(prompt => !prompt.isDone)
  }

  static default() {
    return new Workflow()
  }
}
