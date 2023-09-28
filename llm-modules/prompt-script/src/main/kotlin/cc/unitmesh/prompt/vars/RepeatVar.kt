package cc.unitmesh.prompt.vars

class RepeatVar(
    name: String,
    range: ClosedRange<Int>,
    step: Int,
) : ConnectionVar<Int>(name, "int", range, step)