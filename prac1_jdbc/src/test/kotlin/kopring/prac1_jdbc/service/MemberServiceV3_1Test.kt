package kopring.prac1_jdbc.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kopring.prac1_jdbc.connection.ConnectionConst
import kopring.prac1_jdbc.domain.Member
import kopring.prac1_jdbc.repository.MemberRepoV2
import kopring.prac1_jdbc.repository.MemberRepoV3
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.PlatformTransactionManager

private val logger = KotlinLogging.logger {  }

// 트랜잭션 매니저 사용
class MemberServiceV3_1Test {

    companion object {
        const val MEMBER_A = "memberA"
        const val MEMBER_B = "memberB"
        const val MEMBER_EX = "ex"
    }

    private lateinit var memberRepo : MemberRepoV3
    private lateinit var memberService : MemberServiceV3_1

    @BeforeEach
    fun beforeEach(){
        val dataSource = DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD)
        memberRepo = MemberRepoV3(dataSource)
        val transactionManager = DataSourceTransactionManager(dataSource)
        memberService = MemberServiceV3_1(memberRepo, transactionManager)
    }

    @AfterEach
    fun afterEach(){
        memberRepo.delete(MEMBER_A)
        memberRepo.delete(MEMBER_B)
        memberRepo.delete(MEMBER_EX)
    }

    @Test
    @DisplayName("정상 이체")
    fun accountTransfer(){
        // given
        val memberA = Member(MEMBER_A, 10000)
        val memberB = Member(MEMBER_B, 10000)
        memberRepo.save(memberA)
        memberRepo.save(memberB)

        // when
        memberService.accountTransfer(memberA.memberId, memberB.memberId, 2000)

        // then
        memberRepo.findById(memberA.memberId).money shouldBe 8000
        memberRepo.findById(memberB.memberId).money shouldBe 12000

    }

    @Test
    @DisplayName("이체 중 예외 발생")
    fun accountTransferEx(){
        // given
        val memberA = Member(MEMBER_A, 10000)
        val memberB = Member(MEMBER_EX, 10000)
        memberRepo.save(memberA)
        memberRepo.save(memberB)

        // when
        shouldThrow<IllegalStateException> { memberService.accountTransfer(memberA.memberId, memberB.memberId, 2000) }

        // then
        memberRepo.findById(memberA.memberId).money shouldBe 10000
        memberRepo.findById(memberB.memberId).money shouldBe 10000

    }
}