package kopring.prac3_1_sql.sub_query

import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SubQueryV1Test {

    @Autowired
    private lateinit var subQueryV1 : MySubQuery

    @Test
    fun subQueryTestV1() {
        // given
        val todoId = 1L

        // when
        val result = subQueryV1.subQueryV1(todoId)

        // then
        result.size shouldBeGreaterThan 1
    }

    @Test
    fun subQueryTestV2() {
        // given
        val todoId = 1L

        // when
        val result = subQueryV1.subQueryV2(todoId)

        // then
        result.size shouldBeGreaterThan 1
    }
}