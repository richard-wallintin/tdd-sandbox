package tdd.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SandboxApplication

fun main(args: Array<String>) {
    runApplication<SandboxApplication>(*args)
}
