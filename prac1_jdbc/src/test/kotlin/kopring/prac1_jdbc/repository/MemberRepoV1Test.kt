package kopring.prac1_jdbc.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.equals.shouldBeEqual
import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

private val logger = KotlinLogging.logger {  }

class MemberRepoV1Test {

    val memberRepo = MemberRepoV1()

    @Test
    fun crud() {

        // create
        val member = Member("member_C", 20000)
        memberRepo.save(member)

        // read
        val findMember = memberRepo.findById(member.memberId)
        logger.info { "findMember = $findMember"}

        member shouldBeEqual findMember
    }
}