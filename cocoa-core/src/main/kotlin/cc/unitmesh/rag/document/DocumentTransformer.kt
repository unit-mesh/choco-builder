package cc.unitmesh.rag.document

import java.util.function.Function


interface DocumentTransformer : Function<List<Document>, List<Document>>

