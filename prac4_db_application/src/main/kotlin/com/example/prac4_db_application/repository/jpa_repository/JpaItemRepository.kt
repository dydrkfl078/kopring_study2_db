package com.example.prac4_db_application.repository.jpa_repository

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }

//@Repository
//@Transactional
class JpaItemRepository (
    private val em : EntityManager
): ItemRepository {

    override fun save(item: Item) {
        logger.info { "Mybatis save!" }
        em.persist(item)
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        val item = em.find(Item::class.java, itemId)
        item.apply {
            itemName = updateDto.itemName
            price = updateDto.price
            quantity = updateDto.quantity
        }
    }

    override fun findById(id: Long): Item? {
        return em.find(Item::class.java, id)
    }


    override fun findAll(cond: ItemSearchCond): List<Item> {
        var jpql = "select i from Item i"

        val itemName = cond.itemName
        val maxPrice = cond.maxPrice

        if ( !itemName.isNullOrBlank() || maxPrice != null ) {
            jpql+=" where"
        }

        var andFlag = false
        val param = mutableListOf<Any>()

        if (!itemName.isNullOrBlank()) {
            jpql+= " i.itemName like concat('%',:itemName,'%')"
            param.add(itemName)
            andFlag = true
        }

        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and"
            }
            jpql += " i.price <= :maxPrice"
            param.add(maxPrice)
        }

        logger.info { "find all jpql = $jpql" }

        val query : TypedQuery<Item> = em.createQuery(jpql, Item::class.java).apply {
            if (!itemName.isNullOrBlank()) {
                setParameter("itemName", itemName)
            }
            maxPrice?.let { setParameter("maxPrice", it) }
        }

        return query.resultList.toList()
    }
}