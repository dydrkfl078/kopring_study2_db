package kopring.prac5_spring_transaction.springtx

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class TxInternalCallTestV2 {

    @Autowired
    private lateinit var externalService : TxExternalCallServiceV2

    /*

    내부 호출 시 this.METHOD 와 같은 형태로 실행되므로, 내부에서 호출되던 internalCall 메서드를 클래스를 분리하여 작성
    해당 경우는 AOP Proxy 객체를 사용하므로 externalCall 은 Tx 적용X , internalCall 은 Tx가 적용된다.

    */

    @Test
    fun externalCall() {
        externalService.externalCall()
    }
}

@Service
class TxExternalCallServiceV2 (private val internalService : TxInternalCallServiceV2) {

    fun externalCall() {
        logger.info { "external call Start" }
        isTransactionActive()
        internalService.internalCall()
        logger.info { "external call End" }
    }

    private fun isTransactionActive() {
        logger.info { "tx Active = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    }
}

@Service
class TxInternalCallServiceV2() {

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