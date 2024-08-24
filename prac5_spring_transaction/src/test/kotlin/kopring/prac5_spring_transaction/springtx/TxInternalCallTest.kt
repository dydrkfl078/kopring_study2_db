package kopring.prac5_spring_transaction.springtx

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class TxInternalCallTest {

    @Autowired
    private lateinit var service : TxInternalCallService

    /*

    내부 호출 시 this.METHOD 와 같은 형태로 실행되므로,
    AOP Proxy 의 Transaction 이 적용된 internalCall()이 호출되지 않고, Transaction 이 적용되지 않은 internalCall()이 실행된다.

    */

    @Test
    @DisplayName("맴버 메서드로 접근 시 트랜잭션이 작동하지 않는다 !!Log 확인")
    fun externalCall() {
        service.externalCall()
    }

    @Test
    @DisplayName("외부 메서드로 접근 시 트랜잭션이 작동한다 !!Log 확인")
    fun internalCall() {
        service.internalCall()
    }

}

@Service
class TxInternalCallService () {

    fun externalCall() {
        logger.info { "external call Start" }
        isTransactionActive()
        internalCall()
        logger.info { "external call End" }
    }

    @Transactional
    fun internalCall() {
        logger.info { "internal call Start" }
        isTransactionActive()
        logger.info { "internal call End" }
    }

    private fun isTransactionActive() {
        logger.info { "tx Active = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    }
}