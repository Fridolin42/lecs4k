plugins {
    kotlin("jvm") version "2.3.21"
    `java-library`
    `maven-publish`
}

group = "de.fridolin1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("de.fridolin1:IdCollection:0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}