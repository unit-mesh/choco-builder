package cc.unitmesh.cf.domains.sql.domain

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslBase

class AnalyzedSqlDsl : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslBase>
}
