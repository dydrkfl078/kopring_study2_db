package kopring.prac5_spring_transaction.dto.propagation

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import kopring.prac5_spring_transaction.domain.Log
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }

@Repository
class LogRepository (private val em : EntityManager) {

    @Transactional(propagation = REQUIRES_NEW)
    fun save(log: Log) {
        logger.info { "Log 저장" }
        em.persist(log)

        if (log.message.contains(Log.LOG_EXCEPTION)) {
            logger.info { "${Log.LOG_EXCEPTION} 발생" }
            throw RuntimeException("예외 발생")
        }
    }

    fun findByUserName(message: String): Log? {
        return em.createQuery("select l from Log l where l.message = :message", Log::class.java)
            .setParameter("message", message)
            .resultList.firstOrNull()
    }
}