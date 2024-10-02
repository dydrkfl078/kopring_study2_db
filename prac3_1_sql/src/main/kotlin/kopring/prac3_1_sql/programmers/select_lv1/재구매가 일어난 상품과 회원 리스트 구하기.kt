package kopring.prac3_1_sql.programmers.select_lv1

class `재구매가 일어난 상품과 회원 리스트 구하기` {

    val sql =
        "SELECT USER_ID, PRODUCT_ID\n" +
        "FROM ONLINE_SALE\n" +
        "GROUP BY USER_ID, PRODUCT_ID\n" +
        "HAVING COUNT(PRODUCT_ID) > 1\n" +
        "ORDER BY USER_ID, PRODUCT_ID DESC"
}
