'use client'
import { useState } from 'react'
import { type Message, useChat } from 'ai/react'

import { cn } from '@/lib/utils'
import { ChatPanel } from '@/components/chat-panel'
import { EmptyScreen } from '@/components/empty-screen'
import { ChatScrollAnchor } from '@/components/chat-scroll-anchor'
import { useLocalStorage } from '@/lib/hooks/use-local-storage'
import { ChatList } from '@/components/chat-list'
import { Stage } from '@/components/workflow/stage'
import { Workflow } from '@/components/workflow/workflow'

export interface ChatProps extends React.ComponentProps<'div'> {
  initialMessages?: Message[]
  id?: string
}

export function Chat({ id, initialMessages, className }: ChatProps) {
  const [domain, setDomain] = useLocalStorage<string | null>(
    'ai-domain',
    'Frontend'
  )
  const [stage, setStage] = useState<Stage>(Stage.Clarify)
  const [workflow, setWorkflow] = useState<Workflow>(Workflow.default())

  // sent request to /api/domains/frontend
  const updateDomain = (value: string | null) => {
    setDomain(value)
    setStage(Stage.Classify)
  }

  const { messages, append, reload, stop, isLoading, input, setInput } =
    useChat({
      api: 'http://localhost:18080/api/chat',
      initialMessages,
      id,
      headers: {
        'Content-Type': 'application/json'
      },
      body: {
        stage,
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
