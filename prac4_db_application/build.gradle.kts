plugins {
    kotlin("plugin.jpa") version "1.9.24"
}



dependencies {
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
