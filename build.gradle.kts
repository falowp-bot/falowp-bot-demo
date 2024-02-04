plugins {
    kotlin("jvm") version "2.0.0-Beta2"
    id("io.ktor.plugin") version "3.0.0-beta-1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0-Beta2"
}

group = "com.blr19c.falowp"
version = "1.2.2"

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
}
