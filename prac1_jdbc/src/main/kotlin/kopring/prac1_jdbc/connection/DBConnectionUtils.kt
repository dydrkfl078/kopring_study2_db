package kopring.prac1_jdbc.connection

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.connection.ConnectionConst.PASSWORD
import kopring.prac1_jdbc.connection.ConnectionConst.URL
import kopring.prac1_jdbc.connection.ConnectionConst.USERNAME
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val logger = KotlinLogging.logger {  }

object DBConnectionUtils {

    fun getConnection(): Connection{
        try {
            val connection : Connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)

            logger.info { "get connection= $connection , class = ${connection.javaClass}" }
            return connection
        } catch (e: SQLException){
            throw IllegalStateException(e)
        }
    }
}