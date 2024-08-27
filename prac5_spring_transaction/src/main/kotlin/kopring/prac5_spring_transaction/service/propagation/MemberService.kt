package kopring.prac5_spring_transaction.service.propagation

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac5_spring_transaction.domain.Log
import kopring.prac5_spring_transaction.domain.Member
import kopring.prac5_spring_transaction.dto.propagation.LogRepository
import kopring.prac5_spring_transaction.dto.propagation.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }
@Service
class MemberService (private val memberRepo : MemberRepository,
    private val logRepo : LogRepository
    ){

    @Transactional
    fun joinV1(userName : String) {

        logger.info { "== memberRepository 호출 시작 ==" }
        memberRepo.save(Member(userName))
        logger.info { "== memberRepository 호출 종료 ==" }

        logger.info { "== logRepository 호출 시작 ==" }
        logRepo.save(Log(userName))
        logger.info { "== logRepository 호출 종료 ==" }

    }

    @Transactional
    fun joinV2(userName : String) {

        logger.info { "== memberRepository 호출 시작 ==" }
        memberRepo.save(Member(userName))
        logger.info { "== memberRepository 호출 종료 ==" }

        logger.info { "== logRepository 호출 시작 ==" }
        try {
            logRepo.save(Log(userName))
        } catch (e: RuntimeException) {
            logger.info { "예외 발생 : $e" }
            logger.info { "정상 흐름 반환" }
        }
        logger.info { "== logRepository 호출 종료 ==" }

    }

    fun findByUserName(name: String) = memberRepo.findByUserName(name)
}