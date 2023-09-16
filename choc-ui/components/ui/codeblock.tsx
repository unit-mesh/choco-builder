// Inspired by Chatbot-UI and modified to fit the needs of this project
// @see https://github.com/mckaywrigley/chatbot-ui/blob/main/components/Markdown/CodeBlock.tsx

'use client'

import {FC, memo, useState} from 'react'
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter'
import {coldarkDark} from 'react-syntax-highlighter/dist/cjs/styles/prism'

import {useCopyToClipboard} from '@/lib/hooks/use-copy-to-clipboard'
import {IconCheck, IconCopy, IconDownload} from '@/components/ui/icons'
import {Button} from '@/components/ui/button'
import {InterpreterMessage} from '@/components/interpreter/message'
import {Iframe} from '@/components/interpreter/iframe'
import {DesignBlock} from '@/components/dsl/design-block'
import * as Collapsible from '@radix-ui/react-collapsible';
import { RowSpacingIcon, Cross2Icon } from '@radix-ui/react-icons';

interface Props {
  language: string
  value: string
}

interface languageMap {
  [key: string]: string | undefined
}

export const programmingLanguages: languageMap = {
  javascript: '.js',
  python: '.py',
  java: '.java',
  c: '.c',
  cpp: '.cpp',
  'c++': '.cpp',
  'c#': '.cs',
  ruby: '.rb',
  php: '.php',
  swift: '.swift',
  'objective-c': '.m',
  kotlin: '.kt',
  typescript: '.ts',
  go: '.go',
  perl: '.pl',
  rust: '.rs',
  scala: '.scala',
  haskell: '.hs',
  lua: '.lua',
  shell: '.sh',
  sql: '.sql',
  html: '.html',
  css: '.css'
  // add more file extensions here, make sure the key is same as language prop in CodeBlock.tsx component
}

export const generateRandomString = (length: number, lowercase = false) => {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXY3456789' // excluding similar looking characters like Z, 2, I, 1, O, 0
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return lowercase ? result.toLowerCase() : result
}

const CodeBlock: FC<Props> = memo(({ language, value }) => {
  const [open, setOpen] = useState(false);

  if (language === 'interpreter') {
    // if value is string , convert to json
    if (value && typeof value == 'string') {
      value = JSON.parse(value)
    }

    let msg = value as unknown as InterpreterMessage
    if (msg.msgType == 'html') {
      return <Iframe content={msg.content.html!!} />
    } else {
      if (msg && msg.content && msg.content.type == 'cc.unitmesh.code.messaging.ErrorContent') {
        return (
          <CodeBlock
            language={'json'}
            value={JSON.stringify(msg, null, 2)}
          />
        )
      }

      return <CodeBlock language={'markdown'} value={msg.resultValue} />
    }
  }

  if (language === 'debug') {
    return <Collapsible.Root className="CollapsibleRoot" open={open} onOpenChange={setOpen}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span className="Text" style={{ color: 'black' }}>
          -------------------------- Show debug information --------------------------
        </span>
        <Collapsible.Trigger asChild>
          <button className="IconButton">{open ? <Cross2Icon /> : <RowSpacingIcon />}</button>
        </Collapsible.Trigger>
      </div>

      <Collapsible.Content>
        <CodeBlock language={'markdown'} value={value} />
      </Collapsible.Content>
    </Collapsible.Root>
  }

  if (language === 'design') {
    return <div>
      <CodeBlock language={'origin-design'} value={value} />
      <DesignBlock value={value} />
    </div>
  }

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const { isCopied, copyToClipboard } = useCopyToClipboard({ timeout: 2000 })

  const downloadAsFile = () => {
    if (typeof window === 'undefined') {
      return
    }
    const fileExtension = programmingLanguages[language] || '.file'
    const suggestedFileName = `file-${generateRandomString(
      3,
      true
    )}${fileExtension}`
    const fileName = window.prompt('Enter file name' || '', suggestedFileName)

    if (!fileName) {
      // User pressed cancel on prompt.
      return
    }

    const blob = new Blob([value], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.download = fileName
    link.href = url
    link.style.display = 'none'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
  }

  const onCopy = () => {
    if (isCopied) return
    copyToClipboard(value)
  }

  return (
    <div className="relative w-full font-sans codeblock bg-zinc-950">
      <div className="flex items-center justify-between w-full px-6 py-2 pr-4 bg-zinc-800 text-zinc-100">
        <span className="text-xs lowercase">{language}</span>
        <div className="flex items-center space-x-1">
          <Button
            variant="ghost"
            className="hover:bg-zinc-800 focus-visible:ring-1 focus-visible:ring-slate-700 focus-visible:ring-offset-0"
            onClick={downloadAsFile}
            size="icon"
          >
            <IconDownload />
            <span className="sr-only">Download</span>
          </Button>
          <Button
            variant="ghost"
            size="icon"
            className="text-xs hover:bg-zinc-800 focus-visible:ring-1 focus-visible:ring-slate-700 focus-visible:ring-offset-0"
            onClick={onCopy}
          >
            {isCopied ? <IconCheck /> : <IconCopy />}
            <span className="sr-only">Copy code</span>
          </Button>
        </div>
      </div>
      <SyntaxHighlighter
        language={language}
        style={coldarkDark}
        PreTag="div"
        showLineNumbers
        customStyle={{
          margin: 0,
          width: '100%',
          background: 'transparent',
          padding: '1.5rem 1rem'
        }}
        codeTagProps={{
          style: {
            fontSize: '0.9rem',
            fontFamily: 'var(--font-mono)'
          }
        }}
      >
        {value}
      </SyntaxHighlighter>
    </div>
  )
})
CodeBlock.displayName = 'CodeBlock'

export { CodeBlock }
