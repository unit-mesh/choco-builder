package cc.unitmesh.cf.presentation.ext

import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

class SseEmitterUtf8 : SseEmitter() {
    override fun extendResponse(outputMessage: ServerHttpResponse) {
        super.extendResponse(outputMessage)
        outputMessage.headers.contentType = MediaType(MediaType.TEXT_EVENT_STREAM, Charsets.UTF_8)
    }
}