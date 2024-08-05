package kopring.prac1_jdbc.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import kopring.prac1_jdbc.domain.Member
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.NoSuchElementException

private val logger = KotlinLogging.logger {  }

class MemberRepoV1Test {

    val memberRepo = MemberRepoV1()

    @Test
    fun crud() {

        // create
        val member = Member("member_A", 20000)
        memberRepo.save(member)

        // read
        val findMember = memberRepo.findById(member.memberId)
        logger.info { "findMember = $findMember"}

        member shouldBeEqual findMember

        // update
        memberRepo.update(member.memberId,50000)
        val updateMember = memberRepo.findById(member.memberId)

        updateMember.money shouldBe 50000

        // delete
        memberRepo.delete(member.memberId)

        shouldThrow<NoSuchElementException> {
            memberRepo.findById(member.memberId)
        }


    }
}