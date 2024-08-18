package com.example.prac4_db_application

import com.example.prac4_db_application.config.MemoryConfig
import com.example.prac4_db_application.repository.ItemRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Import(MemoryConfig::class)
@SpringBootApplication(scanBasePackages = ["com.example.prac4_db_application.web"])
class Prac4DbApplication {


    @Bean
    @Profile("local")
    fun testDataInit(itemRepository: ItemRepository): TestDataInit {
        return TestDataInit(itemRepository)
    }
}

fun main(args: Array<String>) {
    runApplication<Prac4DbApplication>(*args)

}



