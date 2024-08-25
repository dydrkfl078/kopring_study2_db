package kopring.prac5_spring_transaction.tx_propagation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.transaction.interceptor.DefaultTransactionAttribute
import org.springframework.transaction.support.DefaultTransactionDefinition
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

@Import(kopring.prac5_spring_transaction.tx_propagation.TestConfiguration::class)
@SpringBootTest
class PropagationTest {

    @Autowired
    private lateinit var txManager: PlatformTransactionManager


    @Test
    @DisplayName("모든 Tx가 commit 일 경우 commit ")
    fun duplicatedTxCommit() {
        logger.info { "first transaction start" }
        val firstTx = txManager.getTransaction(DefaultTransactionDefinition())
        val firstTxIsFirst = firstTx.isNewTransaction
        logger.info { "second transaction start" }
        val secondTx = txManager.getTransaction(DefaultTransactionAttribute())
        val secondTxIsFirst = secondTx.isNewTransaction
        logger.info { "second transaction commit" }
        txManager.commit(secondTx)

        logger.info { "first transaction commit" }
        txManager.commit(firstTx)

        firstTxIsFirst.shouldBeTrue()
        secondTxIsFirst.shouldBeFalse()
    }

    @Test
    @DisplayName("중첩된 Tx 중에 하나라도 rollback 하면 모두 rollback")
    fun firstTxRollback() {
        logger.info { "first transaction start" }
        val firstTx = txManager.getTransaction(DefaultTransactionDefinition())
        val firstTxIsFirst = firstTx.isNewTransaction
        logger.info { "second transaction start" }
        val secondTx = txManager.getTransaction(DefaultTransactionAttribute())
        val secondTxIsFirst = secondTx.isNewTransaction
        logger.info { "second transaction commit" }
        txManager.commit(secondTx)

        logger.info { "first transaction rollback" }
        txManager.rollback(firstTx)

        firstTxIsFirst.shouldBeTrue()
        secondTxIsFirst.shouldBeFalse()
    }
    @Test
    @DisplayName("중첩된 Tx 중에 하나라도 rollback 하면 모두 rollback2")
    fun secondTxRollback() {
        logger.info { "first transaction start" }
        val firstTx = txManager.getTransaction(DefaultTransactionDefinition())
        val firstTxIsFirst = firstTx.isNewTransaction
        logger.info { "second transaction start" }
        val secondTx = txManager.getTransaction(DefaultTransactionAttribute())
        val secondTxIsFirst = secondTx.isNewTransaction
        logger.info { "second transaction rollback" }
        txManager.rollback(secondTx)

        try {
            logger.info { "first transaction commit" }
            txManager.commit(firstTx)
        } catch (e: UnexpectedRollbackException) {
            logger.info { "Transaction is Failure : $e" }
        }

        firstTxIsFirst.shouldBeTrue()
        secondTxIsFirst.shouldBeFalse()
    }

    @Test
    @DisplayName("REQUIRES_NEW 속성의 Tx는 참여자가 아닌, 새로운 Tx를 갖는다.")
    fun requiresNewInnerRollback(){
        val firstTx = txManager.getTransaction(DefaultTransactionDefinition())
        val firstTxIsFirst = firstTx.isNewTransaction

        val secondTx = txManager.getTransaction(DefaultTransactionAttribute().apply {
            propagationBehavior = DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW
        })
        val secondTxIsFirst = secondTx.isNewTransaction

        logger.info { "second transaction rollback" }
        txManager.rollback(secondTx)

        logger.info { "first transaction commit" }
        txManager.commit(firstTx)

        firstTxIsFirst.shouldBeTrue()
        secondTxIsFirst.shouldBeTrue()

    }
}

@TestConfiguration
class TestConfiguration {

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

}