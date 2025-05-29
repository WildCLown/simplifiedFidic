package br.com.baycap.simplifiedfidic

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimplifiedFidicApplication

fun main(args: Array<String>) {
    val dotenv =
        dotenv {
            ignoreIfMissing = false
        }

    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    runApplication<SimplifiedFidicApplication>(*args)
}
