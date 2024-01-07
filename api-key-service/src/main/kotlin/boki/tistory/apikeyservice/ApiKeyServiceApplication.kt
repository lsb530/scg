package boki.tistory.apikeyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiKeyServiceApplication

fun main(args: Array<String>) {
	runApplication<ApiKeyServiceApplication>(*args)
}
