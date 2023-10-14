package org.cc

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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


    @Test
    fun `parses BREAKING CHANGE footers with higher precedence than body`() {
        val parsed = Parser("fix: address major bug\n\nBREAKING CHANGE: this is breaking.\nthis is a free form body of text\nAuthor: @bcoe\nRefs #392").parse()
//        val data = Node(
//            "message", "", Position(Pos(10, 1, 83), Pos(1, 1, 0)),
//            listOf(
//                Node(
//                    "summary", "address major bug", Position(Pos(23, 1, 22), Pos(1, 1, 0))
//                ),
//                Node(
//                    "newline", "\n", Position(Pos(1, 3, 24), Pos(23, 1, 22))
//                ),
//                Node(
//                    "footer", "", Position(Pos(35, 3, 58), Pos(1, 3, 24)),
//                    listOf(
//                        Node(
//                            "token", "BREAKING CHANGE", Position(Pos(16, 3, 39), Pos(1, 3, 24))
//                        ),
//                        Node(
//                            "separator", ":", Position(Pos(17, 3, 40), Pos(16, 3, 39))
//                        ),
//                        Node(
//                            "whitespace", " ", Position(Pos(18, 3, 41), Pos(17, 3, 40))
//                        ),
//                        Node(
//                            "value", "this is breaking.", Position(Pos(35, 3, 58), Pos(18, 3, 41))
//                        )
//                    )
//                ),
//                Node(
//                    "newline", "\n", Position(Pos(1, 5, 60), Pos(35, 3, 58))
//                ),
//                Node(
//                    "footer", "", Position(Pos(14, 5, 73), Pos(1, 5, 60)),
//                    listOf(
//                        Node(
//                            "token", "Author", Position(Pos(7, 5, 66), Pos(7, 5, 66))
//                        ),
//                        Node(
//                            "separator", ":", Position(Pos(8, 5, 67), Pos(7, 5, 66))
//                        ),
//                        Node(
//                            "whitespace", " ", Position(Pos(9, 5, 68), Pos(8, 5, 67))
//                        ),
//                        Node(
//                            "value", "@bcoe", Position(Pos(14, 5, 73), Pos(9, 5, 68))
//                        )
//                    )
//                ),
//                Node(
//                    "newline", "\n", Position(Pos(1, 6, 74), Pos(14, 5, 73))
//                ),
//                Node(
//                    "footer", "", Position(Pos(5, 6, 78), Pos(1, 6, 74)),
//                    listOf(
//                        Node(
//                            "token", "Refs", Position(Pos(5, 6, 78), Pos(5, 6, 78))
//                        ),
//                        Node(
//                            "separator", " #", Position(Pos(7, 6, 80), Pos(5, 6, 78))
//                        ),
//                        Node(
//                            "value", "392", Position(Pos(10, 6, 83), Pos(7, 6, 80))
//                        )
//                    )
//                )
//            )
//        )
//        parsed shouldBe data
        println(Json.encodeToString(parsed))
    }
}