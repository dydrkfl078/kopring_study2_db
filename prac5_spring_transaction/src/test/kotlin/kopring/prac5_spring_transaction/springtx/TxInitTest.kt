package kopring.prac5_spring_transaction.springtx

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import jakarta.annotation.PostConstruct
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

private val logger = KotlinLogging.logger {  }

@SpringBootTest
class TxInitTest {

    @Autowired
    private lateinit var initService : InitService

    @Test
    @DisplayName("Spring 시작 시점에 트랜잭션을 초기화할 경우 Construct Init 은 초기화가 되지 않고, EventListener 는 초기화가 된다 !!Log 확인")
    fun springStart() {
        // Spring Container 가 완성되는 로그를 확인해야함.

        // @PostConstruct - 메서드를 직접 실행하면 AOP 를 호출하는 것이므로, Transaction 이 정상적으로 작동하지만, Spring 시작 시에는 초기화가 되지 않음.
        // post construct init tx = false

        // @EventListener(ApplicationReadyEvent::class) - spring 이 초기화 되는 시점에 초기화가 정상적으로 작동 된다.
        // event listener init tx = true
        // init tx = true
    }

}

@Service
class InitService {

    @PostConstruct
    @Transactional
    fun initFail() {
        logger.info { "post construct init tx = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    }

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initSuccess() {
        logger.info { "event listener init tx = ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    }
}