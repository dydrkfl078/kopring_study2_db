package kopring.prac2_jdbc_exception.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.support.JdbcUtils
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.random.Random

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class ExceptionTranslatorV1Test {

    private lateinit var service : Service
    private lateinit var repo : Repository

    // Junit Test 클래스에는 생성자 주입이 불가능 하다.
    @Autowired
    private lateinit var dataSource: DataSource

    @BeforeEach
    fun init(){
        repo = Repository(dataSource)
        service = Service(repo)
    }

    @Test
    @DisplayName("Primary Key가 동일한 요청이 들어올 경우 예외처리")
    fun duplicateKeySave() {
        val member = Member("rosa",2000)
        service.create(member)
        service.create(member)
    }

    internal class Service(private val repository: Repository) {

        fun create(member: Member) {
            try {
                repository.save(member)
                logger.info { "saveId = ${member.memberId}" }
            } catch (e: MyDuplicateKeyException) {
                logger.info { "키 중복, 복구 시도" }
                val retryId : String = "${member.memberId}${Random.nextInt(10000)}"
                repository.save(Member(retryId, member.money))
            }
        }
    }

    internal class Repository(private val dataSource: DataSource) {

        private lateinit var con : Connection
        private lateinit var pstmt : PreparedStatement
        private lateinit var rs : ResultSet

        fun save(member: Member): Member {
            val sql = "INSERT INTO member(member_id, money) VALUES(?, ?)"

            try {
                con = dataSource.connection
                pstmt = con.prepareStatement(sql).apply {
                    setString(1, member.memberId)
                    setInt(2, member.money)
                    executeUpdate()
                }

                return member
            } catch (e: SQLException) {
                // H2 DB
                if (e.errorCode == 23505) {
                    throw MyDuplicateKeyException(e.message)
                } else {
                    throw e
                }
            } finally {
                if (::rs.isInitialized) {
                    JdbcUtils.closeResultSet(rs)
                }
                if (::pstmt.isInitialized) {
                    JdbcUtils.closeStatement(pstmt)
                }
                if (::con.isInitialized) {
                    JdbcUtils.closeConnection(con)
                }
            }
        }
    }

    internal class MyDuplicateKeyException(message : String?) : Exception() {
        val _message = message

        fun errorMessage() = _message ?: "error message not found"
    }
}