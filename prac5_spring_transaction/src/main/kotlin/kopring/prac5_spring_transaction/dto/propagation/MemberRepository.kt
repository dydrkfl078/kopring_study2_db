package kopring.prac5_spring_transaction.dto.propagation

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import kopring.prac5_spring_transaction.domain.Member
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }
@Repository
class MemberRepository (
    private val em : EntityManager
){

    @Transactional
    fun save(member: Member) {
        logger.info { "Member 저장" }
        em.persist(member)
    }

    fun findByUserName(name: String): Member? {
        return em.createQuery("select m from Member m where m.userName = :username", Member::class.java)
            .setParameter("username", name)
            .resultList.firstOrNull()
    }
}