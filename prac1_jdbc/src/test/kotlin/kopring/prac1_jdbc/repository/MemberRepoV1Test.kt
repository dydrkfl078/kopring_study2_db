package kopring.prac1_jdbc.repository

import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import kopring.prac1_jdbc.connection.ConnectionConst
import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.NoSuchElementException

private val logger = KotlinLogging.logger {  }
class MemberRepoV1Test {

    private lateinit var memberRepo : MemberRepoV1

    @BeforeEach
    fun beforeEach() {

        // driverManagerDataSource 사용
//        val dataSource = DriverManagerDataSource(ConnectionConst.URL,ConnectionConst.USERNAME,ConnectionConst.PASSWORD)

        val dataSource : HikariDataSource = HikariDataSource().apply {
            jdbcUrl = ConnectionConst.URL
            username = ConnectionConst.USERNAME
            password = ConnectionConst.PASSWORD
            maximumPoolSize = 10
            poolName = "MyPool"
        }
        memberRepo = MemberRepoV1(dataSource)
    }

    @Test
    fun crud() {

        // create
        val member = Member("member_A", 20000)
        memberRepo.save(member)

        // read
        val findMember = memberRepo.findById(member.memberId)
        logger.info { "findMember = $findMember"}

        member shouldBeEqual findMember

        // update
        memberRepo.update(member.memberId,50000)
        val updateMember = memberRepo.findById(member.memberId)

        updateMember.money shouldBe 50000

        // delete
        memberRepo.delete(member.memberId)

        shouldThrow<NoSuchElementException> {
            memberRepo.findById(member.memberId)
        }


    }
}