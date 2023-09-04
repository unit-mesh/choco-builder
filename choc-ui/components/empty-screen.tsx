import { ExternalLink } from '@/components/external-link'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { useState } from 'react'
import { domains } from '@/components/workflow/domains'

type EmptyScreenType = {
  setDomain: (value: string | null) => void
}

export function EmptyScreen({ setDomain }: EmptyScreenType) {
  const [value, setValue] = useState(domains[0].value)

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
      </div>
    </div>
  )
}
