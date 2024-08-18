package com.example.prac4_db_application.config

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.memory.MemoryItemRepository
import com.example.prac4_db_application.service.ItemService
import com.example.prac4_db_application.service.ItemServiceV1
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

@Configuration
class MemoryConfig {

    @Bean
    fun itemRepository(): ItemRepository {
        return MemoryItemRepository()
    }

    @Bean
    fun itemService() : ItemService {
        return ItemServiceV1(itemRepository())
    }
}