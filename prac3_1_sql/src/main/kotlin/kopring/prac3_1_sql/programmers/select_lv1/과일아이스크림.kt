package kopring.prac3_1_sql.programmers.select_lv1

class 과일아이스크림 {

    val sql =
        "SELECT FH.FLAVOR\n" +
                "FROM FIRST_HALF AS FH JOIN ICECREAM_INFO AS INFO ON FH.FLAVOR = INFO.FLAVOR\n" +
                "WHERE INFO.INGREDIENT_TYPE = 'fruit_based' AND TOTAL_ORDER > 3000\n" +
                "ORDER BY TOTAL_ORDER DESC"
}