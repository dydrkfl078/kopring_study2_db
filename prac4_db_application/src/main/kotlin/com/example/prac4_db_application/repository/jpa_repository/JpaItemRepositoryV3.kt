package com.example.prac4_db_application.repository.jpa_repository

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.domain.QItem
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class JpaItemRepositoryV3(private val em : EntityManager): ItemRepository {

    private val query = JPAQueryFactory(em)

    override fun save(item: Item) {
        em.persist(item)
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        em.find(Item::class.java, itemId)
            .apply { update(updateDto) }
    }

    override fun findById(id: Long): Item? {
        return em.find(Item::class.java, id)
    }

    fun findAllV1(cond: ItemSearchCond): List<Item> {
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice

        val booleanBuilder = BooleanBuilder()
        if (!itemName.isNullOrBlank()) {
            booleanBuilder.and(QItem.item.itemName.like("%${itemName}%"))
        }

        if (maxPrice != null) {
            booleanBuilder.and(QItem.item.price.loe(maxPrice))
        }

        val result = query
            .select(QItem.item)
            .from(QItem.item)
            .where(booleanBuilder)
            .fetch()

        return result
    }

    // findAll V2
    override fun findAll(con: ItemSearchCond): List<Item> {
        val item = QItem.item

        return query.select(item)
            .from(item)
            .where(likeItemName(con.itemName, QItem.item.itemName), loePrice(con.maxPrice))
            .limit(3)
            .fetch()
    }

    private fun likeItemName(str : String?, path: StringPath) : BooleanExpression? {
        return if (!str.isNullOrBlank()) path.like("%$str%") else null
    }

    private fun loePrice(maxPrice: Int?) : BooleanExpression? {
        return if (maxPrice != null) QItem.item.price.loe(maxPrice) else null
    }
}