package com.example.prac4_db_application

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemRepository
import com.example.prac4_db_application.repository.ItemUpdateDto
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@Transactional // TestClass 에서 Transactional 의 경우 로직이 성공해도 rollback 된다.
@SpringBootTest
class Prac4DbApplicationTests {

    @Autowired
    private lateinit var itemRepo : ItemRepository

    // @Commit Annotation 추가 시 테스트가 종료해도 DB에 데이터가 남아있다.
    @Test
    fun create() {
        // when & given
        val item = itemRepo.save(Item("testItem", 1000, 10))

        // then
        itemRepo.findById(item.id!!)!!.itemName shouldBe "testItem"
    }

    @Test
    fun update(){

        //when
        val item = itemRepo.save(Item("testItem", 1000, 10))
        val editItem = ItemUpdateDto("testItem-1", 5000, 5)

        // given
        itemRepo.update(item.id!!, editItem)

        // then
        itemRepo.findById(item.id!!)?.shouldBeEqual(Item("testItem-1", 5000, 5, item.id))
    }

}
