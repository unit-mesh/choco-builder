const {parser} = require('@conventional-commits/parser')
const ast = parser('feat(parser): add support for scopes #8' +
    'BREAKING CHANGE' +
    '')
console.log(ast)