package kopring.prac1_jdbc.service

import kopring.prac1_jdbc.repository.MemberRepoV1
import kopring.prac1_jdbc.repository.MemberRepoV2

class MemberServiceV1(private val memberRepo : MemberRepoV2) {

    fun accountTransfer(fromId : String, toId : String, money : Int) {
        val fromMember = memberRepo.findById(fromId)
        val toMember = memberRepo.findById(toId)

        memberRepo.update(fromId, fromMember.money - money)
        validation(toId)
        memberRepo.update(toId, toMember.money + money)
    }

    private fun validation(fromId: String) {
        if (fromId == "ex") {
            throw IllegalArgumentException()
        }
    }
}