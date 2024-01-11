val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.0-Beta2"
    id("io.ktor.plugin") version "3.0.0-beta-1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0-Beta2"
}

group = "com.blr19c.falowp"
version = "0.0.1"

application {
    mainClass.set("com.blr19c.falowp.demo.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.blr19c.falowp:falowp-bot-system:1.0.1-beta-4")
}
