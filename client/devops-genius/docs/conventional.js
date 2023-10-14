const fs = require('fs')
const {parser} = require('@conventional-commits/parser')
const ast = parser("fix: a really weird bug")
fs.writeFileSync('../src/test/resources/commits/parses-summary-with-no-scope.json', JSON.stringify(ast, null, 2))

const ast2 = parser("feat(parser): add support for scopes")
fs.writeFileSync('../src/test/resources/commits/parses-summary-with-scope.json', JSON.stringify(ast2, null, 2))