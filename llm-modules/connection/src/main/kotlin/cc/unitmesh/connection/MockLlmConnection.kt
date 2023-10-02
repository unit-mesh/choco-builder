package cc.unitmesh.connection

class MockLlmConnection(val response: String) : BaseConnection(
    name = "mock",
    type = ConnectionType.MockLlm
) {}