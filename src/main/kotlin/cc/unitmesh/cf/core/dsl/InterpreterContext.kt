package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.core.utils.IdUtil
import com.fasterxml.jackson.annotation.JsonGetter
import jakarta.persistence.Id
import org.hibernate.annotations.Comment

abstract class InterpreterContext(
    @Id
    override val id: String = IdUtil.uuid(),

    @Comment("名称")
    @get:JsonGetter("名称")
    override val name: String,

    @Comment("说明")
    @get:JsonGetter("说明")
    val description: String,
) : EmbeddingElement {
}