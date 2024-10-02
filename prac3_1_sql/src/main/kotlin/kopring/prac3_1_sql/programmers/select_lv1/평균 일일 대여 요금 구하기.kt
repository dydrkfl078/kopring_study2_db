package kopring.prac3_1_sql.programmers.select_lv1

class `평균 일일 대여 요금 구하기` {

    val sql =
        "SELECT ROUND(AVG(DAILY_FEE),0)\n" +
                "FROM CAR_RENTAL_COMPANY_CAR AS CAR\n" +
                "WHERE CAR.CAR_TYPE = 'SUV'"
}