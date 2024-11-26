plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

allprojects {
    group = "kopring"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

dependencies {
    // 웹 스타터 추가
    implementation("org.springframework.boot:spring-boot-starter-web")
}



subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
        testImplementation("io.kotest:kotest-assertions-core:5.9.0")
        implementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
        implementation("io.github.oshai:kotlin-logging-jvm:5.1.1")
        runtimeOnly("com.h2database:h2")

        // JDBC
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

        // My Batis
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
        testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.3")

        // JPA
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

        // REDIS
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
    }

    tasks {

        kotlin {
            compilerOptions {
                freeCompilerArgs.addAll("-Xjsr305=strict")
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        java {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }
    }
}

//project("prac1_jdbc") { // 컴파일 시 multi-module-database 로딩
//    dependencies {
//        compileOnly(":kopring_study_db")
//    }
//}
