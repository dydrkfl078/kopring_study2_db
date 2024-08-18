package com.example.prac4_db_application.repository

import com.example.prac4_db_application.domain.Item

interface ItemRepository {
    fun save(item : Item): Item
    fun update(itemId : Long, updateDto : ItemUpdateDto)
    fun findById(id : Long) : Item?
    fun findAll(con : ItemSearchCond) : List<Item>
}