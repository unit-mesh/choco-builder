export interface PromptExample {
  question: string
  answer: string
}

export class QAExample implements PromptExample {
  question: string
  answer: string

  constructor(question: string, answer: string) {
    this.question = question
    this.answer = answer
  }
}

export class QAUpdateExample implements PromptExample {
  question: string
  answer: string
  nextAction: string
  finalOutput: string

  constructor(
    question: string,
    answer: string,
    nextAction: string = '',
    finalOutput: string = ''
  ) {
    this.question = question
    this.answer = answer
    this.nextAction = nextAction
    this.finalOutput = finalOutput
  }
}
