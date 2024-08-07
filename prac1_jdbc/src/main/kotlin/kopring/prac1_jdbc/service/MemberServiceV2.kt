package kopring.prac1_jdbc.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac1_jdbc.repository.MemberRepoV2
import java.sql.Connection
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

class MemberServiceV2(private val memberRepo : MemberRepoV2, private val dataSource: DataSource) {

    fun accountTransfer(fromId : String, toId : String, money : Int) {
        val con = dataSource.connection

        try {
            // 트랜잭션 시작
            con.autoCommit = false

            // 비즈니스 로직
            bizLogic(con, fromId, toId, money)
            con.commit()

        } catch (e: Exception){
            logger.info { "error : $e" }
            con.rollback()
            throw IllegalStateException(e)
        } finally {
            if ( con != null ) {
                try {
                    con.autoCommit = true
                    con.close()
                } catch (e: Exception) {
                    logger.info { " error : $e" }
                }
            }
        }
    }

    private fun bizLogic(con: Connection, fromId: String, toId: String, money: Int) {
        val fromMember = memberRepo.findById(con, fromId)
        val toMember = memberRepo.findById(con, toId)

        memberRepo.update(con, fromId, fromMember.money - money)
        validation(toId)
        memberRepo.update(con, toId, toMember.money + money)
    }

    private fun validation(fromId: String) {
        if (fromId == "ex") {
            throw IllegalArgumentException()
        }
    }
}