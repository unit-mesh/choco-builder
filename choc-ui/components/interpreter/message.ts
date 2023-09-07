export type InterpreterMessage = {
    id: number
    resultValue: string
    displayValue: string
    className: string
    msgType: MsgType
    content: MessageContent
}

export type MsgType = "none" | "error" | "html" | "running"

export type MessageContent = {
    type: string
    // when type is "cc.unitmesh.code.messaging.HtmlContent"
    html?: string
}
