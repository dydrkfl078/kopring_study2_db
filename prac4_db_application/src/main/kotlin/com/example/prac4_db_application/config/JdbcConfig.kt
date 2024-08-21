package com.example.prac4_db_application.config

import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.jdbc_template.JdbcTemplateRepositoryV2
import com.example.prac4_db_application.service.ItemService
import com.example.prac4_db_application.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

//@Configuration
class JdbcConfig (private val dataSource : DataSource){

    @Bean
    fun itemRepository() : ItemRepository {
        return JdbcTemplateRepositoryV2(dataSource)
    }

    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }
}