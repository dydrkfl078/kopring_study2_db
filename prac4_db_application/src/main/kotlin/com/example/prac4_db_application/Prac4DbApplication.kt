package com.example.prac4_db_application

import com.example.prac4_db_application.config.JdbcConfig
import com.example.prac4_db_application.config.MemoryConfig
import com.example.prac4_db_application.repository.ItemRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Import(JdbcConfig::class)
@SpringBootApplication
class Prac4DbApplication {


}

fun main(args: Array<String>) {
    runApplication<Prac4DbApplication>(*args)

}




