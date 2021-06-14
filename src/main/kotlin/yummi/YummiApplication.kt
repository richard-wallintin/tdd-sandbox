package yummi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YummiApplication

fun main(args: Array<String>) {
    runApplication<YummiApplication>(*args)
}
