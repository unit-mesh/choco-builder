---
layout: default
title: Document
parent: Retrieval Augmented Generation
nav_order: 2
---

主要的实现是基于 [Langchain4j](https://github.com/langchain4j/langchain4j) 的实现。

## Document Parser

变化点：返回由 Document 组成的 List，而不是单个 Document。

支持的类型，参考：[DocumentType](https://github.com/unit-mesh/chocolate-factory/blob/master/cocoa-core/src/main/kotlin/cc/unitmesh/rag/document/DocumentType.kt) :

对应的三个解解析类：

- MsOfficeDocumentParser
- PdfDocumentParser
- TextDocumentParser

## PDF

### Adobe PDF extract API [To Spike]

Docs: [https://developer.adobe.com/document-services/docs/overview/pdf-extract-api/](https://developer.adobe.com/document-services/docs/overview/pdf-extract-api/)

![Extract Process](https://developer.adobe.com/document-services/docs/static/18fb6fd7224a217aed770df84103f50c/624f1/extract_process_21.webp)

相关论文：

- [PDFTriage: Question Answering over Long, Structured Documents](https://arxiv.org/abs/2309.08872)
  ，中文翻译：[ PDFTriage:面向长篇结构化文档的问答](https://mp.weixin.qq.com/s/B2Zl9hnAHxohWKRXEBaC9w)

