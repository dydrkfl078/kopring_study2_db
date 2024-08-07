package kopring.prac1_jdbc.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.repository.MemberRepoV2
import kopring.prac1_jdbc.repository.MemberRepoV3
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.sql.Connection
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }


// 트랜잭션 매니저 사용

class MemberServiceV3_1(private val memberRepo : MemberRepoV3, private val transactionManager: PlatformTransactionManager) {

    fun accountTransfer(fromId : String, toId : String, money : Int) {

        // 트랜잭션 시작
        val status = transactionManager.getTransaction(DefaultTransactionDefinition())

        try {

            // 비즈니스 로직
            bizLogic(fromId, toId, money)
            transactionManager.commit(status) // 성공시 커밋

        } catch (e: Exception){
            transactionManager.rollback(status) // 실패시 롤백
            logger.info { "error : $e" }
            throw IllegalStateException(e)
        }
    }

    private fun bizLogic(fromId: String, toId: String, money: Int) {
        val fromMember = memberRepo.findById(fromId)
        val toMember = memberRepo.findById(toId)

        memberRepo.update( fromId, fromMember.money - money)
        validation(toId)
        memberRepo.update( toId, toMember.money + money)
    }

    private fun validation(fromId: String) {
        if (fromId == "ex") {
            throw IllegalArgumentException()
        }
    }
}