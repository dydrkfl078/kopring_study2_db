package kopring.prac6_redis_cache.repository

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}