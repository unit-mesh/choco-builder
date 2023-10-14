package org.cc

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ParserTest {
    @Test
    fun `parses summary with no scope`() {
        val data = Node(
            "message",
            "",
            Position(Pos(24, 1, 23), Pos(1, 1, 0)),
            listOf(
                Node(
                    "summary", "",
                    Position(Pos(24, 1, 23), Pos(1, 1, 0)),
                    listOf(
                        Node(
                            "type", "fix",
                            Position(Pos(4, 1, 3), Pos(1, 1, 0))
                        ),
                        Node(
                            "separator", ":",
                            Position(Pos(5, 1, 4), Pos(4, 1, 3))
                        ),
                        Node(
                            "whitespace", " ",
                            Position(Pos(6, 1, 5), Pos(5, 1, 4))
                        ),
                        Node(
                            "text", "a really weird bug",
                            Position(Pos(24, 1, 23), Pos(6, 1, 5))
                        )
                    )
                )
            )
        )
        val node = Parser("fix: a really weird bug").parse()
        node shouldBeEqualToComparingFields data
    }

    @Test
    fun `parses summary with scope`() {
        val data = Node(
            "message", "", Position(Pos(37, 1, 36), Pos(1, 1, 0)),
            listOf(
                Node(
                    "summary", "", Position(Pos(37, 1, 36), Pos(1, 1, 0)),
                    listOf(
                        Node(
                            "type", "feat",
                            Position(Pos(5, 1, 4), Pos(1, 1, 0))
                        ),
                        Node(
                            "scope", "parser",
                            Position(Pos(12, 1, 11), Pos(6, 1, 5))
                        ),
                        Node(
                            "separator", ":",
                            Position(Pos(14, 1, 13), Pos(13, 1, 12))
                        ),
                        Node(
                            "whitespace", " ",
                            Position(Pos(15, 1, 14), Pos(14, 1, 13))
                        ),
                        Node(
                            "text", "add support for scopes",
                            Position(Pos(37, 1, 36), Pos(15, 1, 14))
                        )
                    )
                )
            )
        )

        val parsed = Parser("feat(parser): add support for scopes").parse()
        parsed shouldBe data
    }


}