package kopring.prac1_jdbc.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.connection.DBConnectionUtils
import kopring.prac1_jdbc.domain.Member
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

private val logger = KotlinLogging.logger {  }

// JDBC - DriverManager 사용해보기

class MemberRepoV0 {

    private lateinit var con : Connection
    private lateinit var pstmt : PreparedStatement
    private lateinit var rs : ResultSet

    fun save(member: Member): Member {
        val sql = "INSERT INTO member(member_id, money) VALUES (?, ?)"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, member.memberId)
            pstmt.setInt(2, member.money)
            pstmt.executeUpdate()
            return member

        } catch (e: SQLException) {
            logger.info { "save exception : $e" }
            throw e
        } finally {
            connectionClose()
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
                val member = Member(rs.getString("member_id"),rs.getInt("money"))
                return member
            } else {
                throw NoSuchElementException("member_id not found = $memberId")
            }
        } catch (e: SQLException) {
            logger.info { "findById exception : $e" }
            throw e
        } finally {
            connectionClose()
        }
    }

    fun update(memberId : String, money : Int) {
        val sql = "UPDATE member SET money = ? WHERE member_id = ?"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setInt(1, money)
            pstmt.setString(2, memberId)
            val resultSize = pstmt.executeUpdate()
            logger.info { "update result $resultSize" }

        } catch (e: SQLException) {
            logger.info { "update exception : $e" }
            throw e
        } finally {
            connectionClose()
        }
    }

    fun delete(memberId : String){
        val sql = "DELETE FROM member WHERE member_id = ?"

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, memberId)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            logger.info { "delete exception : $e" }
        } finally {
            connectionClose()
        }
    }

    private fun connectionClose() {

        pstmt.let {
            try {
                pstmt.close()
            } catch (e: SQLException) {
                logger.info { "error $e" }
            }
        }

        con.let {
            try {
                con.close()
            } catch (e: SQLException) {
                logger.info { "error $e" }
            }
        }

        if (::rs.isInitialized) {
            try {
                rs.close()
            } catch (e: SQLException) {
                logger.info { "error $e" }
            }
        }
    }

    private fun getConnection() = DBConnectionUtils.getConnection()
}