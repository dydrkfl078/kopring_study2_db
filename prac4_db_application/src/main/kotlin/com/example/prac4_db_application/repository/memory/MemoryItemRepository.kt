package com.example.prac4_db_application.repository.memory

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import java.util.stream.Collectors


@Repository
class MemoryItemRepository : ItemRepository {

    companion object {
        val store = HashMap<Long, Item>()
        var sequence: Long = 0L
    }


    override fun save(item: Item): Item {
        item.id = ++sequence
        store[item.id!!] = item
        return item
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        val findItem = findById(itemId)?.let {
            it.apply {
                itemName = updateDto.itemName
                price = updateDto.price
                quantity = updateDto.quantity
            }
        } ?: throw NoSuchElementException("Item with id $itemId not found")

        store[itemId] = findItem
    }

    override fun findById(id: Long): Item? {
        return store[id]
    }

    override fun findAll(con: ItemSearchCond): List<Item> {
        val itemName = con.itemName
        val maxPrice = con.maxPrice

        return store.values.filter { item ->
            if (itemName.isNullOrBlank()) {
                return@filter true
            }
            item.itemName.contains(itemName)
        }.filter { item ->
            maxPrice ?: return@filter true

            item.price <= maxPrice
        }.toList()
    }
}