# Docs Helper

> DocsHelper Will generate a docs folder with all the markdown files in the current directory.

Analysis Step:

1. parse source code from PsiComment
2. parse `@SampleCode` from Test class
3. generate markdown file

Additional feature

1. setup Git Hook
2. run Hook in GitHub Action and local

## LICENSE

findKDoc.kt based on JetBrains' Kotlin Dokka code, which is released under the Apache License, Version 2.0.
code psi based on Ktlint, which is released under the MIT License.

