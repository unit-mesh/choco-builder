package org.cc

// TypeScript version
// class Scanner {
//  constructor (text, pos) {
//    this.text = text
//    this.pos = pos ? { ...pos } : { line: 1, column: 1, offset: 0 }
//  }
class Scanner(
    val text: String,
    var pos: Pos = Pos(1, 1, 0),
) {

    // eof () {
    //    return this.pos.offset >= this.text.length
    //  }
    fun eof() = pos.offset >= text.length

    //   next (n) {
    //    const token = n
    //      ? this.text.substring(this.pos.offset, this.pos.offset + n)
    //      : this.peek()
    //
    //    this.pos.offset += token.length
    //    this.pos.column += token.length
    //
    //    if (isNewline(token)) {
    //      this.pos.line++
    //      this.pos.column = 1
    //    }
    //
    //    return token
    //  }
    fun next(n: Int = 1): String {
        val token = if (n > 0) text.substring(pos.offset, pos.offset + n) else peek()
        pos.offset += token.length
        pos.column += token.length
        if (isNewline(token)) {
            pos.line++
            pos.column = 1
        }
        return token
    }

    //     peek () {
    //    let token = this.text.charAt(this.pos.offset)
    //    // Consume <CR>? <LF>
    //    if (token === CR && this.text.charAt(this.pos.offset + 1) === LF) {
    //      token += LF
    //    }
    //    return token
    //  }
    fun peek(): String {
        var token = text[pos.offset].toString()
        if (token == CR.toString() && text[pos.offset + 1] == LF) {
            token += LF
        }
        return token
    }

    //peekLiteral (literal) {
    //    const str = this.text.substring(this.pos.offset, this.pos.offset + literal.length)
    //    return literal === str
    //  }
    fun peekLiteral(literal: String): Boolean {
        val str = text.substring(pos.offset, pos.offset + literal.length)
        return literal == str
    }
    //   position () {
    //    return { ...this.pos }
    //  }

    fun position() = pos.copy()

    //   rewind (pos) {
    //    this.pos = pos
    //  }
    fun rewind(pos: Pos) {
        this.pos = pos
    }

    //enter (type, content) {
    //    const position = { start: this.position() }
    //    return Array.isArray(content)
    //      ? { type, children: content, position }
    //      : { type, value: content, position }
    //  }
    fun enter(type: String, content: String): Node {
        val position = Position(pos.copy(), pos.copy())
        return Node(type, value = content, position = position)
    }

    fun enter(type: String, content: List<Node>): Node {
        val position = Position(pos.copy(), pos.copy())
        return Node(type, position = position, children = content)
    }

    //  exit (node) {
    //    node.position.end = this.position()
    //    return node
    //  }
    fun exit(node: Node): Node {
        node.position.end = pos.copy()
        return node
    }

    //abort (node, expectedTokens) {
    //    const position = `${this.pos.line}:${this.pos.column}`
    //    const validTokens = expectedTokens
    //      ? expectedTokens.filter(Boolean).join(', ')
    //      : `<${node.type}>`
    //
    //    const error = this.eof()
    //      ? Error(`unexpected token EOF at ${position}, valid tokens [${validTokens}]`)
    //      : Error(`unexpected token '${this.peek()}' at ${position}, valid tokens [${validTokens}]`)
    //
    //    this.rewind(node.position.start)
    //    return error
    //  }
    fun abort(node: Node, expectedTokens: List<String>? = null): Exception {
        val position = "${pos.line}:${pos.column}"
        val validTokens = expectedTokens?.joinToString(", ") ?: "<${node.type}>"
        val error = if (eof()) {
            Exception("unexpected token EOF at $position, valid tokens [$validTokens]")
        } else {
            Exception("unexpected token '${peek()}' at $position, valid tokens [$validTokens]")
        }

        rewind(node.position.start)
        return error
    }
}
