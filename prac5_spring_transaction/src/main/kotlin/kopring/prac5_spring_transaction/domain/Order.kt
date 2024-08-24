package kopring.prac5_spring_transaction.domain

import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order (
    status : String
){

    @Column(name = "status")
    var status : String = status
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0

    fun update(updateStatus : String){
        status = updateStatus
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Order) return false
        if (this === other) return true

        return id == other.id
    }
}