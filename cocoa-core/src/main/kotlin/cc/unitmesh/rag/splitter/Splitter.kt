package cc.unitmesh.rag.splitter

import cc.unitmesh.rag.document.Document
import java.util.function.Function


interface Splitter : Function<List<Document>, List<Document>>

