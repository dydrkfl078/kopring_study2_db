package com.example.prac4_db_application.repository.jpa_repository

import com.example.prac4_db_application.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SpringDataJpaRepository : JpaRepository<Item, Long> {

    fun findByItemNameLike(itemName : String): List<Item>

    fun findByPriceLessThanEqual(price : Int) : List<Item>

    // 쿼리 메서드
    fun findByItemNameLikeAndPriceLessThanEqual(itemName : String, price : Int) : List<Item>

    // 쿼리 직접 실행
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    fun findItems(@Param("itemName") itemName : String, @Param("price")price: Int) : List<Item>
}