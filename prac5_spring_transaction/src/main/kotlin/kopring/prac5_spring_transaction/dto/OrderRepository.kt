package kopring.prac5_spring_transaction.dto

import kopring.prac5_spring_transaction.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : JpaRepository<Order,Long> {

}