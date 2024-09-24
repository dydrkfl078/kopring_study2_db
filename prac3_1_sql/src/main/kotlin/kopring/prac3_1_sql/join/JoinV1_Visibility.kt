package kopring.prac3_1_sql.join

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class JoinV1Visibility(private val dataSource: DataSource) {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val param = mapOf("forumId" to 1)
    private val rowMapper = RowMapper<String> { rs, _ ->
        val userName = rs.getString("user_name")
        userName
    }
    // 암시적 조인 방식으로 초기에 사용하던 레거시 코드. 가장 기초 적임.
    fun implicitV1(): List<String> {

        // 주어진 id 에 해당하는 포럼에 있는 유저들의 이름을 가져오기
        // user Table 과 forum Table 을 조인 하는 것
        val sql =
            "SELECT U.name" +
            "FROM forum AS F ,user AS U" +
            "WHERE F.id = (:forumId) AND F.userId = U.id"

        val result = template.query(sql, param , rowMapper)

        return result
    }

    fun implicitV2(): List<String> {
        val sql =
            "SELECT *" +
            "FROM forum, user"

        val result = template.query(sql,param,rowMapper)

        return result
    }

    // 암시적 조인 방식은 FROM 절에서 테이블 끼리 별다른 구분이 없고, WHERE 절에 Join 조건을 설정하므로 가독성이 좋지 않고, 유지보수가 어렵다.
    // 명시적 조인 방식은 WHERE 절에서 Join 조건이 명시되는 부분을 분리하여 명시적으로 표시하는 방법
    fun explicitV1(): List<String> {

        // JOIN 테이블에 대한 정보를 JOIN 절 안으로 분리하여 작성. 가독성 ↑
        val sql =
            "SELECT U.name" +
                    "FROM forum AS F" +
                    "JOIN user AS U ON F.userId = U.id " +
                    "WHERE F.id = (:forumId)"

        val result = template.query(sql, param , rowMapper)

        return result
    }
}