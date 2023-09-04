'use client'

import { type Message, useChat } from 'ai/react'

import { cn } from '@/lib/utils'
import { ChatPanel } from '@/components/chat-panel'
import { EmptyScreen } from '@/components/empty-screen'
import { ChatScrollAnchor } from '@/components/chat-scroll-anchor'
import { useLocalStorage } from '@/lib/hooks/use-local-storage'
import { toast } from 'react-hot-toast'
import { ChatList } from '@/components/chat-list'

export interface ChatProps extends React.ComponentProps<'div'> {
  initialMessages?: Message[]
  id?: string
}

enum Stage {
  // 问题归类
  Classify = 'Classify',
  // 问题澄清
  Clarify = 'Clarify',
  // 问题分析
  Analyze = 'Analyze',
  // 解决方案设计
  Design = 'Design',
  // 解决方案实现
  Execute = 'Execute',
  Custom = 'Custom'
}

export function Chat({ id, initialMessages, className }: ChatProps) {
  const [domain, setDomain] = useLocalStorage<string | null>(
    'ai-domain',
    'Frontend'
  )

  // sent request to /api/domains/frontend

  const { messages, append, reload, stop, isLoading, input, setInput } =
    useChat({
      api: 'http://localhost:18080/api/chat',
      initialMessages,
      id,
      headers: {
        'Content-Type': 'application/json'
      },
      body: {
        stage: Stage.Clarify,
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
          <EmptyScreen setDomain={setDomain} />
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
