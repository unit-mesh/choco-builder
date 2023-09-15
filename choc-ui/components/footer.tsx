import React from 'react'

import { cn } from '@/lib/utils'
import { ExternalLink } from '@/components/external-link'
import {Span} from "next/dist/server/lib/trace/tracer";

export function FooterText({ className, ...props }: React.ComponentProps<'p'>) {
  return (
    <p
      className={cn(
        'px-2 text-center text-xs leading-normal text-muted-foreground',
        className
      )}
      {...props}
    >
      Open source LLM helper factor built with{' '}
      <ExternalLink href="https://github.com/unit-mesh/chocolate-factory">
        Chocolate Factory
      </ExternalLink>
      .
      <ExternalLink href={'https://beian.miit.gov.cn/'}>闽ICP备17004100号-12</ExternalLink>
    </p>
  )
}
