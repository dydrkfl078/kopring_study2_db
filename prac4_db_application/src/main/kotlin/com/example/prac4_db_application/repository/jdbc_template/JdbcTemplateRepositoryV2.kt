package com.example.prac4_db_application.repository.jdbc_template

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }


// Save 메서드를 SimpleJdbcInsert 통해 간편하게 생성 가능.
class JdbcTemplateRepositoryV2 (private val dataSource : DataSource): ItemRepository {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val jdbcInsert = SimpleJdbcInsert(dataSource).run {
        withTableName("item")
        usingGeneratedKeyColumns("id")
        // usingColumns 는 생략 가능.
        // usingColumns("item_name", "price", "quantity")
    }

    override fun save(item: Item) {
        logger.info { "jdbcTemplate save!" }
        val param = BeanPropertySqlParameterSource(item)
        val key = jdbcInsert.executeAndReturnKey(param)
        item.id = key.toLong()

    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        val sql = "UPDATE item SET item_name = :itemName, price = :price, quantity = :quantity WHERE id = :id"
        val param = MapSqlParameterSource().apply {
            addValue("itemName", updateDto.itemName)
            addValue("price", updateDto.price)
            addValue("quantity", updateDto.quantity)
            addValue("id", itemId)
        }
        template.update(sql, param)
    }

    override fun findById(id: Long): Item? {
        val sql = "SELECT * FROM item WHERE id = :id"
        try {
            val param = mapOf("id" to id)
            val item = template.queryForObject(sql, param, rowMapper() )

            return item
        } catch (e: EmptyResultDataAccessException) {
            return null
        }
    }


    override fun findAll(cond: ItemSearchCond): List<Item> {
        var sql = "SELECT id, item_name, price, quantity FROM item"
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice

        if (!itemName.isNullOrBlank() || maxPrice != null ) {
            sql +=" WHERE"
        }

        var andFlag = false
        val param = mutableListOf<Any>()

        if (!itemName.isNullOrBlank()) {
            sql += " item_name LIKE concat('%',:itemName,'%')"
            param.add(itemName)
            andFlag = true
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " AND"
            }
            sql += " price <= :maxPrice"
            param.add(maxPrice)
        }

        return template.query(sql.trimIndent(),  MapSqlParameterSource().apply {
            addValue("itemName", itemName)
            addValue("maxPrice", maxPrice)
        }, rowMapper(),
        )
    }


    private fun rowMapper(): RowMapper<Item> {
        return DataClassRowMapper.newInstance(Item::class.java)
    }
}