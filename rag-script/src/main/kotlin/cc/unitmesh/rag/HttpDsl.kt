package cc.unitmesh.rag


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.io.File

class HttpDsl() {
    fun get(url: String = "", init: HttpDsl.() -> Any) {

    }
}

object Http {
    private val client = HttpClient()

    /**
     * @param url
     */
    fun get(url: String = "", init: () -> Unit) {
        runBlocking {
            client.get(url) {
                init()
            }
        }
    }

    fun download(url: String, function: () -> Unit): File {
        val tempFile = File.createTempFile("download", "tmp")
        function()
        return tempFile
    }

    fun download(url: String): File {
        val fileName = url.substringAfterLast("/")
        val file = File("temp", fileName)

        if (!File("temp").exists()) {
            File("temp").mkdir()
        }

        if (file.exists()) {
            return file
        }

        runBlocking {
            val httpResponse: HttpResponse = client.get(url) {}
            val responseBody: ByteArray = httpResponse.body()
            file.writeBytes(responseBody)
        }

        return file
    }
}
