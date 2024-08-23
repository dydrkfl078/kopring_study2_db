package com.example.prac4_db_application.repository.jpa_repository

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

//@Repository
//@Transactional
class JpaItemRepositoryV2 (
    private val repository : SpringDataJpaRepository
): ItemRepository {

    override fun save(item: Item) {
        repository.save(item)
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        val item = repository.findById(itemId).orElseThrow{ Exception("Item not found!") }
        item.update(updateDto)
    }

    override fun findById(id: Long): Item? {
        return repository.findById(id).orElseThrow { Exception("Item not found!") }
    }


    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice

        return if (!itemName.isNullOrBlank() && maxPrice != null) {
            repository.findItems("%$itemName%", maxPrice)
        } else if (!itemName.isNullOrBlank()){
            repository.findByItemNameLike("%$itemName%")
        } else if (maxPrice != null) {
            repository.findByPriceLessThanEqual(maxPrice)
        } else {
            repository.findAll()
        }
    }
}