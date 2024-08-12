package kopring.prac3_jdbc_template.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.domain.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

@Repository
class JdbcMemberRepoV1(
    private val dataSource: DataSource,
) {

    private val jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)

    fun save(member: Member): Member {
        val sql = "INSERT INTO member(member_id, money) VALUES (?, ?)"
        jdbcTemplate.update(sql, member.memberId, member.money)

        return member
    }

    fun findById(memberId : String): Member? {
        val sql = "SELECT * FROM member WHERE member_id = ?"
        val rowMapper = RowMapper<Member> { rs, _ ->
            val param1 = rs.getString("member_id")
            val param2 = rs.getInt("money")

            return@RowMapper Member(param1, param2)
        }
        return jdbcTemplate.queryForObject(sql, rowMapper, memberId)
    }

    fun update(memberId : String, money : Int) {
        val sql = "UPDATE member SET money = ? WHERE member_id = ?"
        jdbcTemplate.update(sql, money, memberId)
    }

    fun delete(memberId : String){
        val sql = "DELETE FROM member WHERE member_id = ?"
        jdbcTemplate.update(sql, memberId)
    }
}