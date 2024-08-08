package kopring.prac1_jdbc.service

import kopring.prac1_jdbc.repository.MemberRepoV3
import org.springframework.transaction.annotation.Transactional

// @Transactional AOP 사용

open class MemberServiceV3_3(private val memberRepo : MemberRepoV3) {

    @Transactional
    open fun accountTransfer(fromId : String, toId : String, money : Int) {
        bizLogic(fromId, toId, money)
    }

    private fun bizLogic(fromId: String, toId: String, money: Int) {
        val fromMember = memberRepo.findById(fromId)
        val toMember = memberRepo.findById(toId)

        memberRepo.update( fromId, fromMember.money - money)
        validation(toId)
        memberRepo.update( toId, toMember.money + money)
    }

    private fun validation(fromId: String) {
        if (fromId == "ex") {
            throw IllegalArgumentException()
        }
    }
}