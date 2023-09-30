package cc.unitmesh.connection

class MockLlmConnection : BaseConnection(
    name = "mock",
    type = ConnectionType.MockLlm
) {}