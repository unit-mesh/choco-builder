package cc.unitmesh.prompt

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.app.event.implement.IncludeRelativePath
import org.apache.velocity.runtime.RuntimeConstants
import java.io.StringWriter


class VelocityCompiler : PromptCompiler {
    private val velocityEngine = VelocityEngine()

    init {
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
        velocityEngine.setProperty(
            "classpath.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
        )
        velocityEngine.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath::class.java.getName())
        velocityEngine.init()
    }

    override fun compile(templatePath: String, map: Map<String, Any>): String {
        val template: Template = velocityEngine.getTemplate(templatePath)

        val velocityContext = VelocityContext()

        val sw = StringWriter()
        template.merge(velocityContext, sw);
        return sw.toString()
    }
}