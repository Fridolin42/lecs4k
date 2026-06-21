plugins {
    kotlin("jvm") version "2.3.21"
    `java-library`
    id("com.vanniktech.maven.publish") version "0.37.0"
}

group = "de.fridolin1"
version = "0.2-SNAPSHOT"

repositories {
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api("de.fridolin1:IdCollection:0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}