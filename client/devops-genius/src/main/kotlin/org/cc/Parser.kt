package org.cc

class Parser(val commitText: String) {
    private val scanner = Scanner(commitText.trim())

    fun parse() = message()

    /*
     * <message>       ::= <summary>, <newline>+, <body>, (<newline>+, <footer>)*
     *                  |  <summary>, (<newline>+, <footer>)*
     *                  |  <summary>, <newline>*
     *
     */
    // function message (commitText) {
    //  const scanner = new Scanner(commitText.trim())
    //  const node = scanner.enter('message', [])
    //
    //  // <summary> ...
    //  const s = summary(scanner)
    //  if (s instanceof Error) {
    //    throw s
    //  } else {
    //    node.children.push(s)
    //  }
    //  if (scanner.eof()) {
    //    return scanner.exit(node)
    //  }
    //
    //  let nl
    //  let b
    //  // ... <newline>* <body> ...
    //  nl = newline(scanner)
    //  if (nl instanceof Error) {
    //    throw nl
    //  } else {
    //    node.children.push(nl)
    //    b = body(scanner)
    //    if (b instanceof Error) {
    //      b = null
    //    } else {
    //      node.children.push(b)
    //    }
    //  }
    //  if (scanner.eof()) {
    //    return scanner.exit(node)
    //  }
    //
    //  //  ... <newline>* <footer>+
    //  if (b) {
    //    nl = newline(scanner)
    //    if (nl instanceof Error) {
    //      throw nl
    //    } else {
    //      node.children.push(nl)
    //    }
    //  }
    //  while (!scanner.eof()) {
    //    const f = footer(scanner)
    //    if (f instanceof Error) {
    //      break
    //    } else {
    //      node.children.push(f)
    //    }
    //    nl = newline(scanner)
    //    if (nl instanceof Error) {
    //      break
    //    } else {
    //      node.children.push(nl)
    //    }
    //  }
    //
    //  return scanner.exit(node)
    //}
    fun message(): Node {
        val node = scanner.enter("message", listOf())

        // <summary> ...
        val s = summary()
        if (s == null) {
            throw ScannerException(node, "summary")
        } else {
            node.children += s
        }
        if (scanner.eof()) {
            return scanner.exit(node)
        }

        var nl: Node?
        var b: Node?
        // ... <newline>* <body> ...
        nl = newline()
        if (nl == null) {
            throw ScannerException(node, "newline")
        } else {
            node.children += nl
            b = body()
            if (b == null) {
                b = null
            } else {
                node.children += b
            }
        }
        if (scanner.eof()) {
            return scanner.exit(node)
        }

        //  ... <newline>* <footer>+
        if (b != null) {
            nl = newline()
            if (nl == null) {
                throw nl!!
            } else {
                node.children += nl
            }
        }
        while (!scanner.eof()) {
            val f = footer()
            if (f == null) {
                break
            } else {
                node.children += f
            }
            nl = newline()
            if (nl == null) {
                break
            } else {
                node.children += nl
            }
        }

        return scanner.exit(node)
    }

    /*
     * <summary>      ::= <type> "(" <scope> ")" ["!"] ":" <whitespace>* <text>
     *                 |  <type> ["!"] ":" <whitespace>* <text>
     *
     */
    // function summary (scanner) {
    //  const node = scanner.enter('summary', [])
    //
    //  // <type> ...
    //  const t = type(scanner)
    //  if (t instanceof Error) {
    //    return t
    //  } else {
    //    node.children.push(t)
    //  }
    //
    //  // ... "(" <scope> ")" ...
    //  let s = scope(scanner)
    //  if (s instanceof Error) {
    //    s = null
    //  } else {
    //    node.children.push(s)
    //  }
    //
    //  // ... ["!"] ...
    //  let b = breakingChange(scanner)
    //  if (b instanceof Error) {
    //    b = null
    //  } else {
    //    node.children.push(b)
    //  }
    //
    //  // ... ":" ...
    //  const sep = separator(scanner)
    //  if (sep instanceof Error) {
    //    return scanner.abort(node, [!s && '(', !b && '!', ':'])
    //  } else {
    //    node.children.push(sep)
    //  }
    //
    //  // ... <whitespace>* ...
    //  const ws = whitespace(scanner)
    //  if (!(ws instanceof Error)) {
    //    node.children.push(ws)
    //  }
    //
    //  // ... <text>
    //  node.children.push(text(scanner))
    //  return scanner.exit(node)
    //}
    fun summary(): Node? {
        val node = scanner.enter("summary", listOf())

        // <type> ...
        val t = type()
        if (t == null) {
            return t
        } else {
            node.children += t
        }

        // ... "(" <scope> ")" ...
        var s = scope()
        if (s == null) {
            s = null
        } else {
            node.children += s
        }

        // ... ["!"] ...
        var b = breakingChange()
        if (b == null) {
            b = null
        } else {
            node.children += b
        }

        // ... ":" ...
        val sep = separator()
        if (sep == null) {
            val conditionals = listOfNotNull(
                if (s != null) "(" else null,
                if (b != null) "!" else null,
                ":"
            )

            scanner.abort(node, conditionals)
            return null
        } else {
            node.children += sep
        }

        // ... <whitespace>* ...
        val ws = whitespace()
        if (ws != null) {
            node.children += ws
        }

        // ... <text>
        node.children += text()
        return scanner.exit(node)
    }

    /*
     * <type>         ::= 1*<any UTF8-octets except newline or parens or ["!"] ":" or whitespace>
     */
    // function type (scanner) {
    //  const node = scanner.enter('type', '')
    //  while (!scanner.eof()) {
    //    const token = scanner.peek()
    //    if (isParens(token) || isWhitespace(token) || isNewline(token) || token === '!' || token === ':') {
    //      break
    //    }
    //    node.value += scanner.next()
    //  }
    //  if (node.value === '') {
    //    return scanner.abort(node)
    //  } else {
    //    return scanner.exit(node)
    //  }
    //}
    fun type(): Node? {
        val node = scanner.enter("type", "")
        while (!scanner.eof()) {
            val token = scanner.peek()
            if (isParens(token) || isWhitespace(token) || isNewline(token) || token == "!" || token == ":") {
                break
            }
            node.value += scanner.next()
        }

        if (node.value == "") {
            scanner.abort(node)
            return null
        } else {
            return scanner.exit(node)
        }
    }

    /*
     * <text>         ::= 1*<any UTF8-octets except newline>
     */
    //    function text (scanner) {
    //        const node = scanner.enter('text', '')
    //        while (!scanner.eof()) {
    //            const token = scanner.peek()
    //            if (isNewline(token)) {
    //                break
    //            }
    //            node.value += scanner.next()
    //        }
    //        return scanner.exit(node)
    //    }
    fun text(): Node {
        val node = scanner.enter("text", "")
        while (!scanner.eof()) {
            val token = scanner.peek()
            if (isNewline(token)) {
                break
            }
            node.value += scanner.next()
        }
        return scanner.exit(node)
    }

    /*
     * "(" <scope> ")"        ::= 1*<any UTF8-octets except newline or parens>
     */
    // function scope (scanner) {
    //  if (scanner.peek() !== '(') {
    //    return scanner.abort(scanner.enter('scope', ''))
    //  } else {
    //    scanner.next()
    //  }
    //
    //  const node = scanner.enter('scope', '')
    //
    //  while (!scanner.eof()) {
    //    const token = scanner.peek()
    //    if (isParens(token) || isNewline(token)) {
    //      break
    //    }
    //    node.value += scanner.next()
    //  }
    //
    //  if (scanner.peek() !== ')') {
    //    throw scanner.abort(node, [')'])
    //  } else {
    //    scanner.exit(node)
    //    scanner.next()
    //  }
    //
    //  if (node.value === '') {
    //    return scanner.abort(node)
    //  } else {
    //    return node
    //  }
    //}
    fun scope(): Node? {
        if (scanner.peek() != "(") {
            scanner.abort(scanner.enter("scope", ""))
            return null
        } else {
            scanner.next()
        }

        val node = scanner.enter("scope", "")

        while (!scanner.eof()) {
            val token = scanner.peek()
            if (isParens(token) || isNewline(token)) {
                break
            }
            node.value += scanner.next()
        }

        if (scanner.peek() != ")") {
            scanner.abort(node, listOf(")"))
            return null
        } else {
            scanner.exit(node)
            scanner.next()
        }

        if (node.value == "") {
            scanner.abort(node)
            return null
        } else {
            return node
        }
    }


    /*
     * <body>          ::= [<any body-text except pre-footer>], <newline>, <body>*
     *                  | [<any body-text except pre-footer>]
     */
    // function body (scanner) {
    //  const node = scanner.enter('body', [])
    //
    //  // check except <pre-footer> condition:
    //  const pf = preFooter(scanner)
    //  if (!(pf instanceof Error)) return scanner.abort(node)
    //
    //  // ["BREAKING CHANGE", ":", <whitespace>*]
    //  const b = breakingChange(scanner, false)
    //  if (!(b instanceof Error) && scanner.peek() === ':') {
    //    node.children.push(b)
    //    node.children.push(separator(scanner))
    //    const w = whitespace(scanner)
    //    if (!(w instanceof Error)) node.children.push(w)
    //  }
    //
    //  // [<text>]
    //  const t = text(scanner)
    //  node.children.push(t)
    //  // <newline>, <body>*
    //  const nl = newline(scanner)
    //  if (!(nl instanceof Error)) {
    //    const b = body(scanner)
    //    if (b instanceof Error) {
    //      scanner.abort(nl)
    //    } else {
    //      node.children.push(nl)
    //      Array.prototype.push.apply(node.children, b.children)
    //    }
    //  }
    //  return scanner.exit(node)
    //}
    fun body(): Node? {
        val node = scanner.enter("body", listOf())

        // check except <pre-footer> condition:
        val pf = preFooter()
        if (pf != null) {
            scanner.abort(node)
            return null
        }

        // ["BREAKING CHANGE", ":", <whitespace>*]
        val breaking = breakingChange(false)
        if (breaking != null && scanner.peek() == ":") {
            node.children += breaking
            node.children += separator()!!
            val w = whitespace()
            if (w != null) {
                node.children += w
            }
        }

        // [<text>]
        val t = text()
        node.children += t
        // <newline>, <body>*
        val nl = newline()
        if (nl != null) {
            val body = body()
            if (body == null) {
                scanner.abort(nl)
            } else {
                node.children += nl
                node.children += body.children
            }
        }

        return scanner.exit(node)
    }

    /*
     * <newline>*, <footer>+
     */
    //    function preFooter (scanner) {
    //        const node = scanner.enter('pre-footer', [])
    //        let f
    //                while (!scanner.eof()) {
    //                    newline(scanner)
    //                    f = footer(scanner)
    //                    if (f instanceof Error) return scanner.abort(node)
    //                }
    //        return scanner.exit(node)
    //    }
    fun preFooter(): Node? {
        val node = scanner.enter("pre-footer", listOf())
        var f: Node?
        while (!scanner.eof()) {
            newline()
            f = footer()
            if (f == null) {
                scanner.abort(node)
                return null
            }
        }
        return scanner.exit(node)
    }


    /*
     * <footer>       ::= <token> <separator> <whitespace>* <value>
     */
    // function footer (scanner) {
    //  const node = scanner.enter('footer', [])
    //  // <token>
    //  const t = token(scanner)
    //  if (t instanceof Error) {
    //    return t
    //  } else {
    //    node.children.push(t)
    //  }
    //
    //  // <separator>
    //  const s = separator(scanner)
    //  if (s instanceof Error) {
    //    scanner.abort(node)
    //    return s
    //  } else {
    //    node.children.push(s)
    //  }
    //
    //  // <whitespace>*
    //  const ws = whitespace(scanner)
    //  if (!(ws instanceof Error)) {
    //    node.children.push(ws)
    //  }
    //
    //  // <value>
    //  const v = value(scanner)
    //  if (v instanceof Error) {
    //    scanner.abort(node)
    //    return v
    //  } else {
    //    node.children.push(v)
    //  }
    //
    //  return scanner.exit(node)
    //}
    fun footer(): Node? {
        val node = scanner.enter("footer", listOf())
        val t = token()
        if (t == null) {
            return t
        } else {
            node.children += t
        }

        val s = separator()
        if (s == null) {
            scanner.abort(node)
            return s
        } else {
            node.children += s
        }

        val ws = whitespace()
        if (ws != null) {
            node.children += ws
        }

        val v = value()
        if (v == null) {
            scanner.abort(node)
            return v
        } else {
            node.children += v
        }

        return scanner.exit(node)
    }

    /*
     * <token>        ::= <breaking-change>
     *                 |  <type>, "(" <scope> ")", ["!"]
     *                 |  <type>
     */
    // function token (scanner) {
    //  const node = scanner.enter('token', [])
    //  // "BREAKING CHANGE"
    //  const b = breakingChange(scanner)
    //  if (b instanceof Error) {
    //    scanner.abort(node)
    //  } else {
    //    node.children.push(b)
    //    return scanner.exit(node)
    //  }
    //
    //  // <type>
    //  const t = type(scanner)
    //  if (t instanceof Error) {
    //    return t
    //  } else {
    //    node.children.push(t)
    //    // "(" <scope> ")"
    //    const s = scope(scanner)
    //    if (!(s instanceof Error)) {
    //      node.children.push(s)
    //    }
    //    // ["!"]
    //    const b = breakingChange(scanner)
    //    if (!(b instanceof Error)) {
    //      node.children.push(b)
    //    }
    //  }
    //  return scanner.exit(node)
    //}
    fun token(): Node? {
        val node = scanner.enter("token", listOf())
        val b = breakingChange()
        if (b == null) {
            scanner.abort(node)
        } else {
            node.children += b
            return scanner.exit(node)
        }

        val t = type()
        if (t == null) {
            return null
        } else {
            node.children += t
            val s = scope()
            if (s != null) {
                node.children += s
            }
            val change = breakingChange()
            if (change != null) {
                node.children += change
            }
        }
        return scanner.exit(node)
    }

    /*
     * <breaking-change> ::= "!" | "BREAKING CHANGE" | "BREAKING-CHANGE"
     *
     * Note: "!" is only allowed in <footer> and <summary>, not <body>.
     */
    // function breakingChange (scanner, allowBang = true) {
    //  const node = scanner.enter('breaking-change', '')
    //  if (scanner.peek() === '!' && allowBang) {
    //    node.value = scanner.next()
    //  } else if (scanner.peekLiteral('BREAKING CHANGE') || scanner.peekLiteral('BREAKING-CHANGE')) {
    //    node.value = scanner.next('BREAKING CHANGE'.length)
    //  }
    //  if (node.value === '') {
    //    return scanner.abort(node, ['BREAKING CHANGE'])
    //  } else {
    //    return scanner.exit(node)
    //  }
    //}
    fun breakingChange(allowBang: Boolean = true): Node? {
        val node = scanner.enter("breaking-change", "")
        if (scanner.peek() == "!" && allowBang) {
            node.value = scanner.next()
        } else if (scanner.peekLiteral("BREAKING CHANGE") || scanner.peekLiteral("BREAKING-CHANGE")) {
            node.value = scanner.next("BREAKING CHANGE".length)
        }

        if (node.value == "") {
            scanner.abort(node, listOf("BREAKING CHANGE"))
            return null
        } else {
            return scanner.exit(node)
        }
    }

    /*
    * <value>        ::= <text> <continuation>*
    *                 |  <text>
    */
    // function value (scanner) {
    //  const node = scanner.enter('value', [])
    //  node.children.push(text(scanner))
    //  let c
    //  // <continuation>*
    //  while (!((c = continuation(scanner)) instanceof Error)) {
    //    node.children.push(c)
    //  }
    //  return scanner.exit(node)
    //}
    fun value(): Node {
        val node = scanner.enter("value", listOf())
        node.children += text()

        var c: Node
        while (true) {
            c = continuation() ?: break
            node.children += c
        }

        return scanner.exit(node)
    }

    /*
     * <newline> <whitespace> <text>
     */
    // function continuation (scanner) {
    //  const node = scanner.enter('continuation', [])
    //  // <newline>
    //  const nl = newline(scanner)
    //  if (nl instanceof Error) {
    //    return nl
    //  } else {
    //    node.children.push(nl)
    //  }
    //
    //  // <whitespace> <text>
    //  const ws = whitespace(scanner)
    //  if (ws instanceof Error) {
    //    scanner.abort(node)
    //    return ws
    //  } else {
    //    node.children.push(ws)
    //    node.children.push(text(scanner))
    //  }
    //
    //  return scanner.exit(node)
    //}
    fun continuation(): Node? {
        val node = scanner.enter("continuation", listOf())
        val nl = newline()
        if (nl == null) {
            return nl
        } else {
            node.children += nl
        }

        val ws = whitespace()
        if (ws == null) {
            scanner.abort(node)
            return ws
        } else {
            node.children += ws
            node.children += text()
        }

        return scanner.exit(node)
    }

    /*
     * <separator>    ::= ":" | " #"
     */
    // function separator (scanner) {
    //  const node = scanner.enter('separator', '')
    //  // ':'
    //  if (scanner.peek() === ':') {
    //    node.value = scanner.next()
    //    return scanner.exit(node)
    //  }
    //
    //  // ' #'
    //  if (scanner.peek() === ' ') {
    //    scanner.next()
    //    if (scanner.peek() === '#') {
    //      scanner.next()
    //      node.value = ' #'
    //      return scanner.exit(node)
    //    } else {
    //      return scanner.abort(node)
    //    }
    //  }
    //
    //  return scanner.abort(node)
    //}
    fun separator(): Node? {
        val node = scanner.enter("separator", "")
        // ":"
        if (scanner.peek() == ":") {
            node.value = scanner.next()
            return scanner.exit(node)
        }
        // " #"
        if (scanner.peek() == " ") {
            scanner.next()
            if (scanner.peek() == "#") {
                scanner.next()
                node.value = " #"
                return scanner.exit(node)
            } else {
                scanner.abort(node)
                return null
            }
        }

        scanner.abort(node)
        return null
    }

    /*
     * <whitespace>+   ::= <ZWNBSP> | <TAB> | <VT> | <FF> | <SP> | <NBSP> | <USP>
     */
    // function whitespace (scanner) {
    //  const node = scanner.enter('whitespace', '')
    //  while (isWhitespace(scanner.peek())) {
    //    node.value += scanner.next()
    //  }
    //  if (node.value === '') {
    //    return scanner.abort(node, [' '])
    //  }
    //  return scanner.exit(node)
    //}
    fun whitespace(): Node? {
        val node = scanner.enter("whitespace", "")
        while (isWhitespace(scanner.peek())) {
            node.value += scanner.next()
        }
        if (node.value == "") {
            scanner.abort(node, listOf(" "))
            return null
        }
        return scanner.exit(node)
    }


    /*
      * <newline>+       ::= [<CR>], <LF>
      */
    //function newline (scanner) {
    //  const node = scanner.enter('newline', '')
    //  while (isNewline(scanner.peek())) {
    //    node.value += scanner.next()
    //  }
    //  if (node.value === '') {
    //    return scanner.abort(node, ['<CR><LF>', '<LF>'])
    //  }
    //  return scanner.exit(node)
    //}
    fun newline(): Node? {
        val node = scanner.enter("newline", "")
        while (isNewline(scanner.peek())) {
            node.value += scanner.next()
        }

        if (node.value == "") {
            scanner.abort(node, listOf("<CR><LF>", "<LF>"))
            return null
        }

        return scanner.exit(node)
    }
}