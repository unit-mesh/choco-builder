'use client'

import { type Message, useChat } from 'ai/react'

import { cn } from '@/lib/utils'
import { ChatPanel } from '@/components/chat-panel'
import { EmptyScreen } from '@/components/empty-screen'
import { ChatScrollAnchor } from '@/components/chat-scroll-anchor'
import { useLocalStorage } from '@/lib/hooks/use-local-storage'

export interface ChatProps extends React.ComponentProps<'div'> {
  initialMessages?: Message[]
  id?: string
}

export function Chat({ id, initialMessages, className }: ChatProps) {
  const [previewToken] = useLocalStorage<string | null>('ai-token', null)
  const [domain, setDomain] = useLocalStorage<string | null>(
    'ai-domain',
    'frontend'
  )

  const { messages, append, reload, stop, isLoading, input, setInput } =
    useChat({
      api: 'http://localhost:18080',
      initialMessages,
      id,
      body: {
        id,
        domain,
        previewToken
      }
    })
  return (
    <>
      <div className={cn('pb-[200px] pt-4 md:pt-10', className)}>
        {messages.length ? (
          <>
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
        domain={domain}
        setInput={setInput}
      />
    </>
  )
}
