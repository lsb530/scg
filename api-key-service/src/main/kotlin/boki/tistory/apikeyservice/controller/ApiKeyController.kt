package boki.tistory.apikeyservice.controller


import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.*

data class KeyInfo(
    val apiKey: UUID,
    val expired: Date,
    val ip: String
)

@RestController
class ApiKeyController(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun saveToRedis(key: String, value: Any, duration: Duration?) {
        duration?.let {
            redisTemplate.opsForValue().set(key, value, it)
        } ?: redisTemplate.opsForValue().set(key, value)
    }

    fun getFromRedis(key: String): Any? {
        return redisTemplate.opsForValue()[key]
    }

    @PostMapping("/register")
    fun registerKey(request: HttpServletRequest): ResponseEntity<KeyInfo> {
        val apiKey = UUID.randomUUID()
        val days30 = Duration.ofDays(30)
        saveToRedis(key = apiKey.toString(), value = true, days30)

        val now = Date()
        val expired = Date(now.time + days30.toMillis())
        val ip = request.remoteAddr

        return ResponseEntity.ok(KeyInfo(apiKey = apiKey, expired = expired, ip = ip))
    }
}