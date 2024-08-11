package kopring.prac2_jdbc_exception.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.domain.Member
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.jdbc.support.JdbcUtils
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import java.sql.*
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

// Spring SQLExceptionTranslator 적용
// - Service 에서 SQLException 을 처리하게 되면, JDBC 라이브러리에 종속성이 생기기 때문에 해당 종송석 제거를 위함

class MemberRepoV4(
    private val dataSource : DataSource,
    private val exTranslator: SQLErrorCodeSQLExceptionTranslator
) {

    private lateinit var con : Connection
    private lateinit var pstmt : PreparedStatement
    private lateinit var rs : ResultSet

    fun save(member: Member): Member {
        val sql = "INSERT INTO member(member_id, money) VALUES (?, ?)"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql).apply {
                setString(1, member.memberId)
                setInt(2, member.money)
                executeUpdate()
            }

            return member

        } catch (e: SQLException) {
            logger.info { "save exception : $e" }
            throw exTranslator.translate("save",sql,e) ?: e
        } finally {
            allClose(con,pstmt)
        }
    }

    fun findById(memberId : String): Member {
        val sql = "SELECT * FROM member WHERE member_id = ?"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, memberId)

            rs = pstmt.executeQuery()
            if (rs.next()) {
                val member = Member(rs.getString("member_id"), rs.getInt("money"))
                return member
            } else {
                throw NoSuchElementException("member_id not found = $memberId")
            }
        } catch (e: SQLException) {
            logger.info { "findById exception : $e" }
            throw exTranslator.translate("findById",sql,e) ?: e
        } finally {
            allClose(con, pstmt)
        }
    }

    fun update(memberId : String, money : Int) {
        val sql = "UPDATE member SET money = ? WHERE member_id = ?"

        try {
            pstmt = con.prepareStatement(sql).apply {
                setInt(1, money)
                setString(2, memberId)
            }

            val resultSize = pstmt.executeUpdate()
            logger.info { "update result $resultSize" }

        } catch (e: SQLException) {
            logger.info { "update exception : $e" }
            throw exTranslator.translate("update",sql,e) ?: e
        } finally {
            allClose(con, pstmt)
        }
    }

    fun delete(memberId : String){
        val sql = "DELETE FROM member WHERE member_id = ?"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql).apply {
                setString(1, memberId)
                executeUpdate()
            }
        } catch (e: SQLException) {
            logger.info { "delete exception : $e" }
            throw exTranslator.translate("delete",sql,e) ?: e
        } finally {
            allClose(con,pstmt)
        }
    }

    private fun allClose(con : Connection, stmt : Statement) {
        rsClose()
        JdbcUtils.closeStatement(stmt)
        DataSourceUtils.releaseConnection(con, dataSource)
    }

    private fun rsClose(){

        if (this::rs.isInitialized) {
            try {
                rs.close();
            } catch (e : SQLException) {
                logger.info { "Could not close JDBC ResultSet = $e "}
            } catch (ex : Throwable) {
                logger.info { "Unexpected exception on closing JDBC ResultSet = $ex " }
            }
        }
    }

    private fun getConnection(): Connection {
        val con = DataSourceUtils.getConnection(dataSource)
        return con
    }

}