import java.io.File as File

plugins {
    application
    kotlin("jvm") version "1.4.10"
}

application {
    mainClassName = "io.github.kobi32768.quotebot.MainKt"
}

group = "io.github.kobi32768"
version = File("./src/main/resources/version.txt").readText()

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "io.github.kobi32768.quotebot.MainKt"
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.dv8tion:JDA:4.2.0_168")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-alpha1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
