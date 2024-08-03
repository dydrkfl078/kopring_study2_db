package kopring.prac1_jdbc.connection

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class DBConnectionUtilTest {

    @Test
    fun connection() {
        val connection = DBConnectionUtils.getConnection()

        connection shouldNotBe null
    }
}