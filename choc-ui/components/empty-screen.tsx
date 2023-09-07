import { ExternalLink } from '@/components/external-link'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import React, { useState } from 'react'
import { domains } from '@/components/workflow/domains'
import { Button } from '@/components/ui/button'
import { IconArrowRight } from '@/components/ui/icons'

type EmptyScreenType = {
  setDomain: (value: string | null) => void
  setInput: React.Dispatch<React.SetStateAction<string>>
}

export function EmptyScreen({ setDomain, setInput }: EmptyScreenType) {
  const [value, setValue] = useState(domains[0].value)
  const [examples, setExamples] = useState(domains[0].examples)

  return (
    <div className="mx-auto max-w-2xl px-4">
      <div className="rounded-lg border bg-background p-8">
        <h1 className="mb-2 text-lg font-semibold">
          Welcome to use Chocolate Factory!
        </h1>
        <p className="mb-2 leading-normal text-muted-foreground">
          This is an open source LLM application engine designed to empower you
          in creating your very own AI assistant. Built with{' '}
          <ExternalLink href="https://unitmesh.cc">Unit Mesh Team</ExternalLink>
          .
        </p>
        <p className="leading-normal text-muted-foreground">
          You can start with select a LLM helper here:
        </p>
        <div className="mt-4 flex flex-col items-start space-y-2">
          <Select
            value={value}
            onValueChange={value => {
              setValue(value)
              setDomain(value)
              setExamples(domains.find(d => d.value === value)?.examples)
            }}
          >
            <SelectTrigger>
              <SelectValue placeholder="Select a domain…" aria-label={value}>
                {domains.find(d => d.value === value)?.label}
              </SelectValue>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {domains.map(domain => (
                  <SelectItem
                    key={domain.value}
                    value={domain.value}
                    disabled={domain.value === value}
                  >
                    {domain.label}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <p className="leading-normal text-muted-foreground">
          You can start a conversation here or try the following examples:
        </p>
        {examples!! &&
          examples.map((message, index) => (
            <Button
              key={index}
              variant="link"
              className="h-auto p-0 text-base"
              onClick={() => setInput(message.content)}
            >
              <IconArrowRight className="mr-2 text-muted-foreground" />
              {message.title}
            </Button>
          ))}
      </div>
    </div>
  )
}
