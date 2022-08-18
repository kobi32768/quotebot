import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group   = "io.github.kobi32768"
version = File("./src/main/resources/version.txt").readText()

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "io.github.kobi32768.quotebot.MainKt"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.17")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("io.github.kobi32768.quotebot.MainKt")
}
