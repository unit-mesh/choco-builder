'use client'
import React, { useEffect, useState } from 'react'
import { type Message, useChat } from 'ai/react'

import { cn } from '@/lib/utils'
import { ChatPanel } from '@/components/chat-panel'
import { EmptyScreen } from '@/components/empty-screen'
import { ChatScrollAnchor } from '@/components/chat-scroll-anchor'
import { ChatList } from '@/components/chat-list'
import { StageContext, Workflow } from '@/components/workflow/workflow'
import { domains } from '@/components/workflow/domains'
import { Stage } from '@/components/workflow/stage'
import { OpenAIStream, StreamingTextResponse } from 'ai'

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

  useEffect(() => {
    fetch(`http://localhost:18080/api/workflows/${domain}`)
      .then(res => res.json())
      .then(data => {
        setWorkflow(data)
        setPromptStage(data?.length ? data[0] : null)
      })
  }, [domain])

  const updateDomain = (value: string | null) => {
    setDomain(value)
  }

  const { messages, append, reload, stop, isLoading, input, setInput } =
    useChat({
      api: 'http://localhost:18080/api/chat',
      initialMessages,
      id,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      body: {
        stage: promptStage?.stage ?? Stage.Classify,
        id,
        domain
      }
    })
  return (
    <>
      <div className={cn('pb-[200px] pt-4 md:pt-10', className)}>
        {messages.length ? (
          <>
            <ChatList messages={messages} />
            <ChatScrollAnchor trackVisibility={isLoading} />
          </>
        ) : (
          <EmptyScreen setDomain={updateDomain} />
        )}
      </div>
      <ChatPanel
        id={id}
        isLoading={isLoading}
        stop={stop}
        append={append}
        reload={reload}
        messages={messages}
        input={input}
        setInput={setInput}
      />
    </>
  )
}
