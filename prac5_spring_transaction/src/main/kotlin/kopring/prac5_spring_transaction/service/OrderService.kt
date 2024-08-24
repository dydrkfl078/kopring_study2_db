package kopring.prac5_spring_transaction.service

import kopring.prac5_spring_transaction.domain.Order
import kopring.prac5_spring_transaction.dto.OrderRepository
import kopring.prac5_spring_transaction.exception.BusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService (private val orderRepository : OrderRepository) {

    companion object {
        const val BUSINESS_ERROR = "잔고부족"
        const val RUNTIME_ERROR = "에러"
        const val STATUS_HOLD = "대기"
    }

    @Transactional(noRollbackFor = [BusinessException::class])
    fun save(order: Order) {
        orderRepository.save(order)

        if (order.status == BUSINESS_ERROR) {

            order.update(STATUS_HOLD)
            throw BusinessException()
        } else if (order.status == RUNTIME_ERROR) {

            throw RuntimeException()
        }
    }

    fun findById(id: Long): Order? {
        return orderRepository.findById(id).orElse(null)
    }
}