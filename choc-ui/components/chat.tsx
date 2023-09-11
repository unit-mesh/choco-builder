'use client'
import React, { useEffect, useState } from 'react'

import { cn } from '@/lib/utils'
import { ChatPanel } from '@/components/chat-panel'
import { EmptyScreen } from '@/components/empty-screen'
import { ChatScrollAnchor } from '@/components/chat-scroll-anchor'
import { ChatList } from '@/components/chat-list'
import { StageContext, Workflow } from '@/components/workflow/workflow'
import { domains } from '@/components/workflow/domains'
import { Stage } from '@/components/workflow/stage'
import { useChat } from '@/components/flow/use-chat'
import { Message } from 'ai'
import { MessageResponse } from '@/components/workflow/workflow-dto'
import { toast } from 'react-hot-toast'

export interface ChatProps extends React.ComponentProps<'div'> {
  initialMessages?: Message[]
  id?: string
}

export function Chat({ id, initialMessages, className }: ChatProps) {
  const [domain, setDomain] = useState<string | null>(domains[0].value)
  const [workflow, setWorkflow] = useState<Workflow>(Workflow.default())
  const [promptStage, setPromptStage] = useState<StageContext | null>(
    workflow?.prompts?.length ? workflow.prompts[0] : null
  )
  const [stage, setStage] = useState<Stage>(
    promptStage?.stage ?? Stage.Classify
  )

  useEffect(() => {
    setStage(promptStage?.stage ?? Stage.Classify)
  }, [domain])

  useEffect(() => {
    fetch(`/api/workflows/${domain}`)
      .then(res => res.json())
      .then(data => {
        setWorkflow(data)
        let stage: StageContext = data?.length ? data[0] : null
        if (stage) {
          setPromptStage(stage)
          setStage(stage.stage ?? Stage.Classify)
        }
      })
  }, [domain])

  const updateDomain = (value: string | null) => {
    setDomain(value)
  }

  const { messages, append, reload, stop, isLoading, input, setInput } =
    useChat({
      api: '/api/chat',
      initialMessages,
      id,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'X-Experimental-Stream-Data': 'true'
      },
      body: {
        stage,
        id,
        domain
      },
      onError: (err: any) => {
        console.error(err)
        toast.error(JSON.stringify(err))
      },
      onFinish: (data: any) => {
        let msgResponse: MessageResponse = data['object']
        if (msgResponse.result!!) {
          setStage(msgResponse.result?.nextStage!!)
        }
      }
    })
  return (
    <>
      <div className={cn('pb-[200px] pt-4 md:pt-10', className)}>
        {messages.length ? (
          <>
            <ChatList messages={messages as Message[]} />
            <ChatScrollAnchor trackVisibility={isLoading} />
          </>
        ) : (
          <EmptyScreen setDomain={updateDomain} setInput={setInput} />
        )}
      </div>
      <ChatPanel
        id={id}
        isLoading={isLoading}
        stop={stop}
        append={append}
        reload={reload}
        messages={messages as Message[]}
        input={input}
        setInput={setInput}
      />
    </>
  )
}
