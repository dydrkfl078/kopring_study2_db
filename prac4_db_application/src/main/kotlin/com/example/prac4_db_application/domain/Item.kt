package com.example.prac4_db_application.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Item(
    itemName : String,
    price : Int,
    quantity: Int
) {
    @Column(name = "item_name", nullable = false)
    var itemName : String = itemName

    @Column(name = "price", nullable = false)
    var price : Int = price

    @Column(name = "quantity", nullable = false)
    var quantity : Int = quantity

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        return id != null && id == other.id
    }
}