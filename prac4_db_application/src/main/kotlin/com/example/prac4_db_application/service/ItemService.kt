package com.example.prac4_db_application.service

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto

interface ItemService {
    fun save(item : Item): Item
    fun update(itemId: Long, updateDto : ItemUpdateDto)
    fun findById(id : Long) : Item?
    fun findItems(searchCond: ItemSearchCond) : List<Item>
}