# Conventional Commits

代码基于 [conventional-commits-parser](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-commits-parser#conventional-commits-parser), MIT 协议。

使用方式：

1.声明依赖

```groovy
dependencies {
    implementation("cc.unitmesh:git-commit-message:0.4.0")
}
```

2.使用（默认参数）：

```kotlin
import org.changelog.CommitParser
import org.changelog.ParserOptions
import org.changelog.Commit

val parser = CommitParser()
val result: Commit = parser.parse("feat(hello/world): message")
```

3.自定义参数：

```kotlin
import org.changelog.CommitParser
import org.changelog.ParserOptions
import org.changelog.Commit

val customOptions: ParserOptions = ParserOptions(
    revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (.*)\\.$"),
    revertCorrespondence = listOf("header", "hash"),
    fieldPattern = Regex("^-(.*?)-$"),
    headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
    headerCorrespondence = listOf("type", "scope", "subject"),
    noteKeywords = listOf("BREAKING AMEND"),
    issuePrefixes = listOf("#", "gh-"),
    referenceActions = listOf("kill", "kills", "killed", "handle", "handles", "handled")
)

val customParser = CommitParser(customOptions)
val result: Commit = customParser.parse("feat(hello/world): message")
```