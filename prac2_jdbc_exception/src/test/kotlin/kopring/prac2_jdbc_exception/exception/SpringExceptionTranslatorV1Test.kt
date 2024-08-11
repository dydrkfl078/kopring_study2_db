package kopring.prac2_jdbc_exception.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource


private val logger = KotlinLogging.logger {  }

@SpringBootTest
class SpringExceptionTranslatorV1Test {

    @Autowired
    private lateinit var dataSource: DataSource

    private lateinit var con : Connection
    private lateinit var pstmt : PreparedStatement

    @Test
    fun springTranslator() {
        val sql = "SELECT badgrammgar member"

        try {
            con = dataSource.connection
            pstmt = con.prepareStatement(sql)
        } catch (e: SQLException) {

            // 에러 코드 정리하는 소스코드 경로 → org.springframework.jdbc.support → sql-error-codes.xml
            val exTranslator = SQLErrorCodeSQLExceptionTranslator(dataSource)
            val resultEx = exTranslator.translate("SELECT", sql, e)
            logger.info { "resultEx = $resultEx" }
            resultEx.shouldBeInstanceOf<BadSqlGrammarException>()
        }


    }
}