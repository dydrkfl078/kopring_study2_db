rootProject.name = "kopring_study_db"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("prac1_jdbc")
include("prac2_jdbc_exception")
include("prac3_jdbc_template")
include("prac4_db_application")
include("prac5_spring_transaction")
include("prac6_redis_cache")