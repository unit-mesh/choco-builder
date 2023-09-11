import { DesignDslRender } from '@/components/dsl/design-dsl-render'
import { useEffect, useState } from 'react'
import {CodeBlock} from "@/components/ui/codeblock";

interface DslOutput {
  name: string
  description: string
  output: any
  dsl: any
}

export function DesignBlock({ value }: { value: string }) {
  const [dsl, setDsl] = useState<DesignInformation | null >(null)

  useEffect(() => {
    fetch(`/api/dsl/Design`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      body: JSON.stringify({ dsl: value })
    })
      .then(res => res.json())
      .then(data => {
        setDsl(data.output)
      })
  }, [])

  if (!dsl) {
    return <CodeBlock language={'pre-design'} value={value} />
  }

  console.log(dsl);

  if (dsl.layouts.length == 0) {
    return <CodeBlock language={'markdown'} value={value} />
  }

  return <DesignDslRender dsl={dsl} />
}
