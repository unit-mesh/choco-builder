package cc.unitmesh.cf.core.llms

enum class TemperatureMode(val value: Double) {
    Creative(0.7),
    Balanced(0.3),
    Default(0.0),
}