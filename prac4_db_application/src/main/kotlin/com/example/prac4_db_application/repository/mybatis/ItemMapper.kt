package com.example.prac4_db_application.repository.mybatis

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ItemMapper {

    // Parameter 1개인 경우에는 @Param Annotation 을 생략할 수 있다.
    fun save (item : Item)

    fun update(@Param("id") id : Long, @Param("updateParam") itemUpdateDto : ItemUpdateDto)

    fun findById(id : Long) : Item?

    fun findAll(searchCond: ItemSearchCond) : List<Item>

}