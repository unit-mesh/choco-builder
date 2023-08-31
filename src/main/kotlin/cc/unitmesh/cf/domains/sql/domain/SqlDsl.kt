package cc.unitmesh.cf.domains.sql.domain

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter

class SqlDsl(
    val type: String,
    val views: List<String>,
    val select: List<Column>,
    val where: List<Filter>,
    val orderBy: List<Sort>,
    val limit: String?,
) : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslInterpreter>

    lateinit var from: List<Table>

    data class Table(
        val description: String,
        val name: String = "",
    )

    data class Column(
        val description: String,
        var name: String = "",
        var type: String = "",
    )

    class Filter(
        val column: Column,
        var operation: String,
        var value: String,
        var alternatives: List<String> = emptyList(),
    )

    class Sort(
        val column: Column,
        val direction: String,
    )
}
