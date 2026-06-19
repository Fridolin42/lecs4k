plugins {
    kotlin("jvm") version "2.3.21"
}

group = "de.fridolin1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("de.fridolin1:IdMap:0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}