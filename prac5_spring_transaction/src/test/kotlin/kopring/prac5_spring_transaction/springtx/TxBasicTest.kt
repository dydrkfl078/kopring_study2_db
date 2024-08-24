package kopring.prac5_spring_transaction.springtx

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class TxBasicTest {

    @Autowired
    private lateinit var txService : TxService

    @Test
    fun txAopTest() {
        logger.info { "txService class = ${txService::class}" }
        AopUtils.isAopProxy(txService).shouldBeTrue()
    }

    @Test
    fun txBasicTest(){
        txService.useTx()
        txService.nonTx()
    }
}

@Service
class TxService {

    @Transactional
    fun useTx() {
        logger.info { "uesTx Start" }
        logger.info { "tx Active = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
        logger.info { "uesTx End" }
    }

    fun nonTx() {
        logger.info { "nonTx Start" }
        logger.info { "tx Active = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
        logger.info { "nonTx End" }
    }
}