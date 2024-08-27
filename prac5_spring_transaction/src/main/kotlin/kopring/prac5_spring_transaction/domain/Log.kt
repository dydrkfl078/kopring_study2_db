package kopring.prac5_spring_transaction.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Log (
    message : String
){

    companion object {
        const val LOG_EXCEPTION = "로그 예외"
    }

    @Id
    @GeneratedValue
    var id : Long? = null

    var message = message
        protected set
}