package com.example.prac4_db_application.service

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import org.springframework.stereotype.Service

@Service
class ItemServiceV1(private val itemRepository: ItemRepository): ItemService {

    override fun save(item: Item): Item {
        itemRepository.save(item)
        return item
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        itemRepository.update(itemId, updateDto)
    }

    override fun findById(id: Long): Item? {
        return itemRepository.findById(id)
    }

    override fun findItems(searchCond: ItemSearchCond): List<Item> {
        return itemRepository.findAll(searchCond)
    }

}