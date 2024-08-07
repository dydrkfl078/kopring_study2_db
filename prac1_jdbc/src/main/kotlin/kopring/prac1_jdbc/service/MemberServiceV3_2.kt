package kopring.prac1_jdbc.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.repository.MemberRepoV3
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import java.sql.SQLException

private val logger = KotlinLogging.logger {  }

// 트랜잭션 템플릿 사용

class MemberServiceV3_2(private val memberRepo : MemberRepoV3, private val transactionManager: PlatformTransactionManager) {

    private val txTemplate = TransactionTemplate(transactionManager)

    fun accountTransfer(fromId : String, toId : String, money : Int) {

        txTemplate.executeWithoutResult {
            try {
                bizLogic(fromId, toId, money)
            } catch (e: SQLException) {
                throw IllegalStateException(e)
            }
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