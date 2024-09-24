package kopring.prac3_1_sql.sub_query

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

@Component
class MySubQuery(private val dataSource: DataSource) {

    private val template = NamedParameterJdbcTemplate(dataSource)

    // V1. WHERE 절에 서브쿼리 적용
    fun subQueryV1(todoId: Long): List<String?> {
        val sql =
            "SELECT task_value " +
            "FROM todolist_task_value " +
            "WHERE todolist_task_id IN ( SELECT todolist_task.id FROM todolist_task WHERE todolist_task.todolist_id = 1 ) AND todolist_attribute_id IN ( SELECT todolist_attribute.id FROM todolist_attribute WHERE todolist_attribute.todolist_id = (:todoId))"
        val param = mapOf("todoId" to todoId)
        val rowMapper = RowMapper<String?> { rs, rowNum ->
            val result = rs.getString("task_value")
            result ?: null
        }

        return template.query(sql, param, rowMapper)
    }

    // V2. FROM 절데 SubQuery 결과로 얻은 테이블을 추가하는 방법.
    fun subQueryV2(todoId: Long): List<String?> {
        val sql =
            "SELECT task_value\n" +
                    "FROM todolist_task_value,\n" +
                    "( SELECT todolist_task.id FROM todolist_task WHERE todolist_task.todolist_id = (:todoId) ) AS T\n" +
                    "WHERE todolist_task_id = T.id"
        val param = mapOf("todoId" to todoId)
        val rowMapper = RowMapper<String?> { rs, rowNum ->
            val result = rs.getString("task_value")
            result ?: null
        }

        return template.query(sql, param, rowMapper)
    }

    // Exists()
    // --> Exists 의 경우, 서브 쿼리의 결과를 True, False 로 구분하며, 결과의 갯수에 크게 상관하지 않는다.
    fun subQueryExists() {
        val sql = "" +
                "SELECT todolist.id" +
                "FROM todolist AS LIST" +
                "WHERE EXISTS(" +
                "   SELECT *" +
                "   FROM todolist_attribute AS ATB" +
                "   WHERE ATB.todolist_id = LIST.id AND category_name = Like CONCAT ('%',:searchCategory,'%')" +
                ")"
        val param = ""
    }
}