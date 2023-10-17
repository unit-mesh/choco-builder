---
layout: default
title: Document
parent: Retrieval Augmented Generation
nav_order: 10
permalink: /rag/document
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# DocumentParser 

> > 当前的 Chocolate Factory 主要基于 [Langchain4j](https://github.com/langchain4j/langchain4j) 的实现。

Parse the given input stream and return a list of documents.

返回多个 [Document]:
- [cc.unitmesh.rag.document.DocumentType.PPT]

返回单个 [Document]:
- [cc.unitmesh.rag.document.DocumentType.PDF]
- [cc.unitmesh.rag.document.DocumentType.TXT]
- [cc.unitmesh.rag.document.DocumentType.HTML]
- [cc.unitmesh.rag.document.DocumentType.DOC]


## MdDocumentParser 

Markdown Document Parser

This class represents a parser for Markdown documents.

The MdDocumentParser class uses the [MarkdownHeaderTextSplitter] class to split the text of the document
into separate sections based on the headers in the Markdown syntax. It then returns a list of Document
objects representing each section of the document.

Example usage:
```
val parser = MdDocumentParser()
val inputStream = FileInputStream("document.md")
val documents = parser.parse(inputStream)
```



Sample: 

```kotlin
val parser = MdDocumentParser()
val inputStream = ByteArrayInputStream("Sample Markdown Text".toByteArray())
val result = parser.parse(inputStream)
```

## MsOfficeDocumentParser 

The `MsOfficeDocumentParser` class is responsible for extracting text from Microsoft Office documents.
It supports various file formats, including ppt, pptx, doc, docx, xls, and xlsx.
This class implements the `DocumentParser` interface.

For detailed information on the supported formats, please refer to the official Apache POI website: [https://poi.apache.org/](https://poi.apache.org/).



## PdfDocumentParser 

PdfDocumentParser is a class that implements the DocumentParser interface and is used to parse PDF documents.

This class provides a method to parse a given input stream containing a PDF document and returns a list of Document objects.
Each Document object represents a parsed document and contains the extracted content along with additional metadata.

The parse method reads the input stream, loads the PDF document using the Loader class, and extracts the text content using the PDFTextStripper class.
The extracted content is then used to create a Document object with the document type set to PDF.

If an IOException occurs during the parsing process, a RuntimeException is thrown.


## TextDocumentParser 

The `TextDocumentParser` class is responsible for parsing text documents.
It implements the `DocumentParser` interface.



