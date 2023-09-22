package cc.unitmesh.tools.web;

import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class MyServiceTest {


    @Test
    @Ignore
    fun should_return_url_list_when_run_with_valid_title() {
        Wikimedia().run("Intellij").forEach { println(it) }
    }
}