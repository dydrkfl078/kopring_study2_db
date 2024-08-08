package kopring.prac1_jdbc.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kopring.prac1_jdbc.domain.Member
import kopring.prac1_jdbc.repository.MemberRepoV3
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import javax.sql.DataSource


// Spring boot - dataSource, transactionManager 자동 등록.

private val logger = KotlinLogging.logger {  }


@SpringBootTest
class MemberServiceV3_4Test() {

    @org.springframework.boot.test.context.TestConfiguration
    internal class TestConfiguration(private val dataSource: DataSource) {

        @Bean
        fun memberRepo(): MemberRepoV3 {
            return MemberRepoV3(dataSource)
        }

        @Bean
        fun memberService(): MemberServiceV3_3 {
            return MemberServiceV3_3(memberRepo())
        }

    }

    companion object {
        const val MEMBER_A = "memberA"
        const val MEMBER_B = "memberB"
        const val MEMBER_EX = "ex"
    }

    @Autowired
    private lateinit var  memberRepo: MemberRepoV3

    @Autowired
    private lateinit var  memberService: MemberServiceV3_3

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
        shouldThrow<IllegalArgumentException> {
            memberService.accountTransfer(
                memberA.memberId,
                memberB.memberId,
                2000
            )
        }

        // then
        memberRepo.findById(memberA.memberId).money shouldBe 10000
        memberRepo.findById(memberB.memberId).money shouldBe 10000

    }

    @Test
    fun aopCheck(){
        logger.info { "memberService class = ${memberService.javaClass}" }
        logger.info { "memberRepo class = ${memberRepo.javaClass}" }
        AopUtils.isAopProxy(memberService) shouldBe(true)
        AopUtils.isAopProxy(memberRepo) shouldBe(false)

    }
}