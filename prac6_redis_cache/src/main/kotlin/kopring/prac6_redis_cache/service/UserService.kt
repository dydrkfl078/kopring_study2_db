package kopring.prac6_redis_cache.service

import kopring.prac6_redis_cache.repository.User
import kopring.prac6_redis_cache.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun getUser(id: String): User? {
        return userRepository.findByIdOrNull(id)
    }
}