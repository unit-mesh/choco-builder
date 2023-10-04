package cc.unitmesh.connection

/**
 * MockLlmConnection is a mock connection for testing in local.
 * For example:
 * ```yaml
 * name: mock_response
 * type: MockLlm
 *
 * configs:
 *   api-response: "{\"text\": \"this is a mock resource\"}"
 * ```
 */
class MockLlmConnection(val response: String) : ConnectionConfig(
    name = "mock",
    type = ConnectionType.MockLlm
) {}