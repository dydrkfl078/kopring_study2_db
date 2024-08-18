package com.example.prac4_db_application

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

@Component
class TestDataInit (private val itemRepository : ItemRepository) {

    @EventListener
    fun initDate (event: ApplicationReadyEvent) {
        logger.info { "Test data init" }
        itemRepository.save( Item("itemA",10000,10,1))
        itemRepository.save( Item("itemB",20000,20,2))
        itemRepository.save( Item("itemC",30000,30,3))
    }
}