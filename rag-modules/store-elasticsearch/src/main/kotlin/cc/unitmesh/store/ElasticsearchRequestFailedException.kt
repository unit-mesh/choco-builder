package cc.unitmesh.store

internal class ElasticsearchRequestFailedException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
