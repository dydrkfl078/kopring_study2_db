package kopring.prac5_spring_transaction.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Member (
    userName : String
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    var userName = userName
        protected set
}