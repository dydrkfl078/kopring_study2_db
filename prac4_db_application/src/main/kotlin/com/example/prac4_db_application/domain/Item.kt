package com.example.prac4_db_application.domain

data class Item(
    var itemName : String,
    var price : Int,
    var quantity : Int,
    var id : Long? = null
)