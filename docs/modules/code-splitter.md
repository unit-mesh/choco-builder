---
layout: default
title: Code Splitter
parent: Modules
nav_order: 2
permalink: /docs/code-splitter
---

参考内容：[https://docs.sweep.dev/blogs/chunking-2m-files](https://docs.sweep.dev/blogs/chunking-2m-files)

## 基础知识

Code Splitter 模块是一个代码分割模块，用于将代码分割成多个片段，以便进行各种代码相关任务，如代码相似度计算、代码分类、代码聚类等。

### 拆分策略

根据《[Chunking 2M+ files a day for Code Search using Syntax Trees](https://docs.sweep.dev/blogs/chunking-2m-files)》 的建议：

1. 代码的平均 Token 到字符比例约为1:5（300 个 Token），而嵌入模型的 Token 上限为 512 个。
2. 1500 个字符大约对应于 40 行，大致相当于一个小到中等大小的函数或类。
3. 挑战在于尽可能接近 1500 个字符，同时确保分块在语义上相似且相关上下文连接在一起。

### 拆分分工

多种不同方式：

- 基于关键字分割：LangChain
- 经典语法分析
    - Antlr
- 基于规则 DSL的语法分析：LlamaIndex
    - TreeSitter: [https://tree-sitter.github.io/tree-sitter/](https://tree-sitter.github.io/tree-sitter/)

## Chocolate Factory 实现

Chunk/Document 拆分策略

1. 如果代码类少于 1500 个字符，则将整个代码类作为一个代码块。
2. 如果代码类大于 1500 个字符，则将代码类分成多个函数代码块。

代码块逻辑

1. 代码块的第一部分是上下文相关的注释，诸如包名、类名等。 
2. 代码块的第二部分是代码块的内容。

示例：

```chunk
// canonicalName: com.google.common.collect.ImmutableList

public static <T> ImmutableList<T> of(T... elements) {
    List<T> list = new ArrayList<>();
    Collections.addAll(list, elements);
    return new ImmutableList<>(list);
}
```

### Chapi 模型

> [Chapi](https://github.com/modernizing/chapi) is A common language meta information convertor, convert different
> languages to same meta-data model. 一个通用语言元信息转换器，能将不同语言转换为相同的模型。

### 扫描

```
java -jar scanner_cli/build/libs/scanner_cli-2.0.5-all.jar --language=Kotlin --output=http --server-url=http://localhost:18080 --
path=/Volumes/source/ai/chocolate-factory --with-function-code
```

## LangChain 实现

[RecursiveCharacterTextSplitter](https://js.langchain.com/docs/modules/data_connection/document_transformers/text_splitters/code_splitter)

LangChain 支持多种不同的标记和编程语言特定的文本分割器，以根据语言特定的语法来分割您的文本。这会产生更具语义自洽性的分块，对矢量存储或其他检索器更有用。
流行的语言如JavaScript、Python、Solidity和Rust都得到支持，同时还支持Latex、HTML和Markdown。

1. 使用顶层分隔符（首先使用 class ，然后是 function definitions，然后是 method methods 等）来分割文本。 
2. 遍历每个部分，并贪婪地将它们连接在一起，直到达到字符限制。对于太大的分块，可以使用递归方法，以下一个级别的分隔符开始分块。

代码示例：

```python
delimiters = ["\nclass ", "\ndef ", "\n\tdef ", "\n\n", "\n", " ", ""]
def chunk(text: str, delimiter_index: int = 0, MAX_CHARS: int = 1500) -> list[str]:
	delimiter = delimiters[delimiter_index]
	new_chunks = []
	current_chunk = ""
	for section in text.split(delimiter):
		if len(section) > MAX_CHARS:
			# Section is too big, recursively chunk this section
			new_chunks.append(current_chunk)
			current_chunk = ""
			new_chunks.extend(chunk(section, delimiter_index + 1, MAX_CHARS)
		elif len(current_chunk) + len(section) > MAX_CHARS:
			# Current chunk is max size
			new_chunks.append(current_chunk)
			current_chunk = section
		else:
			# Concatenate section to current_chunk
			current_chunk += section
	return new_chunks
```

## LlamaIndex 实现

代码：[CodeSplitter](https://github.com/jerryjliu/llama_index/blob/main/llama_index/text_splitter/code_splitter.py)

关键逻辑：

1. 要分块一个父节点，我们遍历其子节点并贪婪地将它们捆绑在一起。对于每个子节点： 
   - 如果当前分块太大，我们将其添加到分块列表中并清空捆绑。 
   - 如果下一个子节点太大，我们递归地分块子节点并将其添加到分块列表中。 否则，将当前分块与子节点连接起来。
2. 最后，通过将单行分块与下一个分块合并来对最终结果进行后处理。 
   - 这保证了没有分块太小，因为它们会产生不太有意义的结果。

代码示例：

```python
from tree_sitter import Node
 
def chunk_node(node: Node, text: str, MAX_CHARS: int = 1500) -> list[str]:
	new_chunks = []
	current_chunk = ""
	for child in node.children:
		if child.end_byte - child.start_byte > MAX_CHARS:
			new_chunks.append(current_chunk)
			current_chunk = ""
			new_chunks.extend(chunk_node(child, text, MAX_CHARS)
		elif  > MAX_CHARS:
			new_chunks.append(current_chunk)
			current_chunk = text[node.start_byte:node.end_byte]
		else:
			current_chunk += text[node.start_byte:node.end_byte]
	return new_chunks
```
