plugins {
    kotlin("plugin.jpa") version "1.9.24"
}



dependencies {
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.annotation:jakarta.annotation-api")

    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")


}
