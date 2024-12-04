plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "com.blr19c.falowp"
version = "2.0.0"

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
    implementation("com.blr19c.falowp:falowp-bot-adapter-cq:${project.version}")
}
