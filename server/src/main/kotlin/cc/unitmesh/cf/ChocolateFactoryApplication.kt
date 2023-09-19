package cc.unitmesh.cf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ChocolateFactoryApplication

fun main(args: Array<String>) {
    runApplication<ChocolateFactoryApplication>(*args)
}
