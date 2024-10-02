package kopring.prac3_1_sql.programmers.select_lv1

class `흉부외과 또는 일반외과 의사 목록 출력하기` {

    // format 형식의 y 소문자 시 년도가 98 / 01 처럼 작성됨, Y 대문자 시 4자리 년도 표기.
    val sql =
        "SELECT DR_NAME, DR_ID, MCDP_CD, DATE_FORMAT(HIRE_YMD, '%Y-%m-%d')\n" +
                "FROM DOCTOR\n" +
                "WHERE MCDP_CD IN('CS','GS')\n" +
                "ORDER BY HIRE_YMD DESC, DR_NAME"
}