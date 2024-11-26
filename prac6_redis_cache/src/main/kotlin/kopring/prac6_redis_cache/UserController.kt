package kopring.prac6_redis_cache

import kopring.prac6_redis_cache.repository.User
import kopring.prac6_redis_cache.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/users")
    fun createUser(@RequestBody user: User): User {
        return userService.saveUser(user)
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: String): User? {
        return userService.getUser(id)
    }
}