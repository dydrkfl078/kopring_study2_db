package kopring.prac1_jdbc.connection

import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import javax.sql.DataSource

import kopring.prac1_jdbc.connection.ConnectionConst.PASSWORD
import kopring.prac1_jdbc.connection.ConnectionConst.URL
import kopring.prac1_jdbc.connection.ConnectionConst.USERNAME
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.sql.Connection
import java.sql.DriverManager

private val logger = KotlinLogging.logger {  }

class ConnectionTest {

    @Test
    fun driverManager() {
        val con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD)
        val con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD)
        logger.info { "connection = $con1, class = ${con1.javaClass}" }
        logger.info { "connection = $con2, class = ${con2.javaClass}" }
    }

    @Test
    fun dataSourceDriverManager(){
        val dataSource = DriverManagerDataSource(URL, USERNAME, PASSWORD)
        useDriverManager(dataSource)
    }

    @Test
    fun hikariDataSource(){
        val dataSource = HikariDataSource().apply {
            jdbcUrl = URL
            username = USERNAME
            password = PASSWORD
            maximumPoolSize = 10
            poolName = "MyPool"
        }

        useDriverManager(dataSource)
        Thread.sleep(1000L)
    }


    private fun useDriverManager(dataSource : DataSource){
        val con1 : Connection = dataSource.connection
        val con2 : Connection = dataSource.connection
        logger.info { "connection = $con1, class = ${con1.javaClass}" }
        logger.info { "connection = $con2, class = ${con2.javaClass}" }
    }
}