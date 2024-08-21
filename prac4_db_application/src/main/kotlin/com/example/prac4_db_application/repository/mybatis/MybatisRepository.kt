package com.example.prac4_db_application.repository.mybatis

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Repository

private val logger = KotlinLogging.logger {  }

@Repository
class MybatisRepository (private val itemMapper: ItemMapper): ItemRepository {

    override fun save(item: Item) {
        logger.info { "Mybatis save!" }
        itemMapper.save(item)
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        return itemMapper.update(itemId, updateDto)
    }

    override fun findById(id: Long): Item? {
        return itemMapper.findById(id)
    }


    override fun findAll(cond: ItemSearchCond): List<Item> {
        return itemMapper.findAll(cond)
    }
}