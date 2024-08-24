package kopring.prac5_spring_transaction.tx_rollback

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kopring.prac5_spring_transaction.domain.Order
import kopring.prac5_spring_transaction.exception.BusinessException
import kopring.prac5_spring_transaction.service.OrderService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderTest {

    @Autowired
    private lateinit var orderService : OrderService

    @Test
    fun successfulOrder(){
        // given
        val order = Order("정상")

        // when
        orderService.save(order)

        // then
        orderService.findById(order.id)?.shouldBeEqual(order)
    }

    @Test
    fun businessExOder(){
        // given
        val order = Order(OrderService.BUSINESS_ERROR)

        // when
        shouldThrow<BusinessException> {
            orderService.save(order)

        }

        // then
        orderService.findById(order.id)?.status shouldBe (OrderService.STATUS_HOLD)

    }

    @Test
    fun runtimeExOrder(){
        // given
        val order = Order(OrderService.RUNTIME_ERROR)

        // when
        shouldThrow<RuntimeException> {
            orderService.save(order)
        }

        // then
        orderService.findById(order.id)?.shouldBeNull()
    }

}