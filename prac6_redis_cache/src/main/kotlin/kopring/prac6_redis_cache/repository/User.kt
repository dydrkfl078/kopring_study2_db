package kopring.prac6_redis_cache.repository

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash


@RedisHash("User", timeToLive = 300)
data class User(

    @Id
    val id: Int,
    val name: String,
    val age: Int
)
