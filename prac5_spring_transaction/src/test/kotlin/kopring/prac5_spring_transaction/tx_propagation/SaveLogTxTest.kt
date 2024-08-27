package kopring.prac5_spring_transaction.tx_propagation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kopring.prac5_spring_transaction.domain.Log
import kopring.prac5_spring_transaction.dto.propagation.LogRepository
import kopring.prac5_spring_transaction.dto.propagation.MemberRepository
import kopring.prac5_spring_transaction.service.propagation.MemberService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SaveLogTxTest {

    @Autowired
    private lateinit var memberService : MemberService

    @Autowired
    private lateinit var memberRepo : MemberRepository

    @Autowired
    private lateinit var logRepo : LogRepository

    /*
    MemberService == OFF
    MemberRepo    == ON
    LogRepo       == ON
     */
    @Test
    fun outerTxOff_success() {
        // given
        val userName = "outerTxOff"

        // when
        memberService.joinV1(userName)

        // then
        memberService.findByUserName(userName).shouldNotBeNull()
        logRepo.findByUserName(userName)?.id shouldBe(userName)

    }

    /*
        MemberService == OFF
        MemberRepo    == ON
        LogRepo       == ON
     */
    @Test
    fun outerTxOff_fail() {
        // given
        val userName = "${Log.LOG_EXCEPTION}outerTxOff"

        // when
        shouldThrow<RuntimeException> { memberService.joinV1(userName) }

        // then
        memberService.findByUserName(userName)?.shouldNotBeNull()
        logRepo.findByUserName(userName)?.shouldBeNull()
    }

    /*
        MemberService == ON
        MemberRepo    == ON
        LogRepo       == ON
    */
    @Test
    fun singleTx() {
        // given
        val userName = "${Log.LOG_EXCEPTION}singleTxOn"

        // when
        shouldThrow<RuntimeException> { memberService.joinV1(userName) }

        // then
        memberService.findByUserName(userName)?.shouldBeNull()
        logRepo.findByUserName(userName)?.shouldBeNull()
    }

    /*
        MemberService == ON
        MemberRepo    == ON
        LogRepo       == ON - Propagation = Requires_new
    */
    @Test
    fun singleTxPropagation() {
        // given
        val userName = "${Log.LOG_EXCEPTION}singleTxOnPropagation"

        // when
        memberService.joinV2(userName)

        // then
        memberService.findByUserName(userName)?.shouldNotBeNull()
        logRepo.findByUserName(userName)?.shouldBeNull()
    }
}