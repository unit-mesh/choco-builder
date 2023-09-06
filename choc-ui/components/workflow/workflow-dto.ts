import {Message} from "ai";
import {Stage} from "@/components/workflow/stage";

export type MessageResponse = {
    id: string
    result?: WorkflowResult
    created: number
    messages: Message[]
}

export type WorkflowResult = {
    currentStage: Stage
    nextStage: Stage
    responseMsg: string
    resultType: string
    result: string
}
