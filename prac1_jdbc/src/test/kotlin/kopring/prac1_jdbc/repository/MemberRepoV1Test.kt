package kopring.prac1_jdbc.repository

import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MemberRepoV1Test {

    val memberRepo = MemberRepoV1()

    @Test
    fun save() {
        val member = Member("member_A", 10000)
        memberRepo.save(member)
    }
}