plugins {
    java
    kotlin("jvm") version "1.4.31"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


val kotlinVersion: String by project
val kotlinXVersion: String by project
val ktorVersion: String by project
val gsonVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinXVersion)

    api("io.ktor", "ktor-client-core", ktorVersion)
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    api("com.google.code.gson", "gson", gsonVersion)

    testCompile("junit", "junit", "4.12")
}
