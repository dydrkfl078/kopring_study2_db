package com.example.prac4_db_application.repository.jdbc_template

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.util.Objects
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

@Repository
class JdbcTemplateRepository (private val dataSource : DataSource): ItemRepository {

    private val template = NamedParameterJdbcTemplate(dataSource)

    override fun save(item: Item): Item {
        logger.info { "jdbcTemplate save!" }
        val sql = "INSERT INTO item(item_name, price, quantity) VALUES (:itemName, :price, :quantity)"
        val keyHolder = GeneratedKeyHolder()
        val param = BeanPropertySqlParameterSource(item)
        template.update (sql, param, keyHolder )

        item.id = keyHolder.key?.toLong()
        return item
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

        if (param.isEmpty()) {
            return template.query(sql, rowMapper())
        }

        return template.query(sql.trimIndent(),  MapSqlParameterSource().apply {
            addValue("itemName", itemName)
            addValue("maxPrice", maxPrice)
        }, rowMapper(),
        )
    }


    private fun rowMapper(): RowMapper<Item> {
        val rowMapper: RowMapper<Item> = RowMapper { rs, _ ->
            Item(
                rs.getString("item_name"),
                rs.getInt("price"),
                rs.getInt("quantity"),
                rs.getLong("id")
            )
        }
        return rowMapper
    }
}