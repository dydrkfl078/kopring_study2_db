package kopring.prac5_spring_transaction.springtx

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class TxExceptionText {

    @Autowired
    private lateinit var service : NoRollbackService

    @Test
    @DisplayName("설정한 예외는 해당 예외가 발생해도 커밋 - !!Log 확인")
    fun noRollback() {
        shouldThrow<MyException> {
            service.myException()
        }
    }
}

class MyException : RuntimeException() {

}

@Service
class NoRollbackService(){

    @Transactional(noRollbackFor = [MyException::class])
    fun myException(){
        throw MyException()
    }
}
