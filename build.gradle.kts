plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.jpa") version "2.1.21" // Für JPA Entities
}

group = "org.cubow"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.4.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")

    implementation("ch.qos.logback:logback-classic:1.5.13")

    implementation("net.dv8tion:JDA:5.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.json:json:20230618")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}