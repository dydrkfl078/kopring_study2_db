package kopring.prac3_1_sql.join

import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JoinV1Test {

    @Autowired
    private lateinit var service : JoinV1Visibility

    @Test
    fun implicit(){
        val result = service.implicitV1()

        result.size shouldBeGreaterThan 1
    }

    @Test
    fun explicit(){
        val result = service.explicitV1()

        result.size shouldBeGreaterThan 1
    }
}