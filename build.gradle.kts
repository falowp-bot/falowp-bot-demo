plugins {
    kotlin("jvm") version "2.3.0"
    id("io.ktor.plugin") version "3.4.0"
    id("com.github.ben-manes.versions") version "0.53.0"
}

group = "com.blr19c.falowp"
version = "2.3.0"

application {
    mainClass.set("com.blr19c.falowp.demo.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.blr19c.falowp:falowp-bot-system:${project.version}")
    implementation("com.blr19c.falowp:falowp-bot-adapter-nc:${project.version}")
}
