package com.example.prac4_db_application.repository.jdbc_template

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

@Repository
class JdbcTemplateRepository (private val dataSource : DataSource): ItemRepository {

    private val template = JdbcTemplate(dataSource)

    override fun save(item: Item): Item {
        logger.info { "jdbcTemplate save!" }
        val sql = "INSERT INTO item(item_name, price, quantity) VALUES (?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        template.update ({ con ->
            val ps = con.prepareStatement(sql, arrayOf("id"))
                .apply {
                    setString(1,item.itemName)
                    setInt(2,item.price)
                    setInt(3, item.quantity)
                }
            return@update ps
        },keyHolder )

        item.id = keyHolder.key?.toLong()
        return item
    }

    override fun update(itemId: Long, updateDto: ItemUpdateDto) {
        val sql = "UPDATE item SET item_name = ?, price = ?, quantity = ? WHERE id = ?"

        template.update(sql,
            updateDto.itemName,
            updateDto.price,
            updateDto.quantity,
            itemId)
    }

    override fun findById(id: Long): Item? {
        val sql = "SELECT * FROM item WHERE id = ?"
        try {
            val item = template.queryForObject(sql, rowMapper(), id )

            return item
        } catch (e: EmptyResultDataAccessException) {
            return null
        }
    }


    override fun findAll(con: ItemSearchCond): List<Item> {
        val sql = "SELECT * FROM item"
        try {
            return template.query(sql, rowMapper())
        } catch (e: EmptyResultDataAccessException) {
            return emptyList()
        }
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