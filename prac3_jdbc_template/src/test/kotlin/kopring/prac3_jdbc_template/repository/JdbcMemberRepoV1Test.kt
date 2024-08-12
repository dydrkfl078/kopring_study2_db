package kopring.prac3_jdbc_template.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class JdbcMemberRepoV1Test {

    @Autowired
    private lateinit var jdbcMemberRepoV1 : JdbcMemberRepoV1

    @Test
    fun jdbcSave(){
        val member = Member("roe2", 5000)

        jdbcMemberRepoV1.save(member)


    }

}