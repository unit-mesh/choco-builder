const fs = require('fs')
const {parser} = require('@conventional-commits/parser')
const ast = parser("fix: a really weird bug")
fs.writeFileSync('../src/test/resources/commits/parses-summary-with-no-scope.json', JSON.stringify(ast, null, 2))

const ast2 = parser("feat(parser): add support for scopes")
fs.writeFileSync('../src/test/resources/commits/parses-summary-with-scope.json', JSON.stringify(ast2, null, 2))

// supports multiline BREAKING CHANGES, via continuation
const multipleLine = parser("fix: address major bug\nBREAKING CHANGE: first line of breaking change\n second line of breaking change\n third line of breaking change")
var filename = 'supports-multiline-breaking-changes-via-continuation.json'
fs.writeFileSync('../src/test/resources/commits/' + filename, JSON.stringify(multipleLine, null, 2))

// parses summary with multiple spaces after separator
const multipleSpaces = parser("feat(tree):    add whitespace node")
var filename = 'parses-summary-with-multiple-spaces-after-separator.json'
fs.writeFileSync('../src/test/resources/commits/' + filename, JSON.stringify(multipleSpaces, null, 2))

// parses commit summary footer
const summaryFooter = parser("chore: contains multiple commits\nfix(parser): address bug with parser")
var filename = 'parses-commit-summary-footer.json'
fs.writeFileSync('../src/test/resources/commits/' + filename, JSON.stringify(summaryFooter, null, 2))

// allows for multiple newlines between summary and body
const multipleNewlines = parser("fix: address major bug\n\nthis is a free form body of text")
var filename = 'allows-for-multiple-newlines-between-summary-and-body.json'
fs.writeFileSync('../src/test/resources/commits/' + filename, JSON.stringify(multipleNewlines, null, 2))