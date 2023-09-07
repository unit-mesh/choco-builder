package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.utils.Constants
import cc.unitmesh.cf.infrastructure.utils.IdUtil
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Comment

@Entity
@Comment("自动补全 API 缓存")
class CompletionCache(
    @Id
    val id: String = IdUtil.uuid(),

    @Column(length = Constants.MAX_TOKEN_LENGTH, unique = true)
    @Comment("输入消息列表")
    val messages: String,

    @Column(length = Constants.MAX_TOKEN_LENGTH)
    @Comment("补全结果")
    val completion: String,
)
