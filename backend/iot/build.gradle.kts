plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.bakalover.iot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.redisson:redisson:3.29.0")
    //    implementation("io.ktor:ktor-server-core:2.3.10")
//    implementation("io.ktor:ktor-server-netty:2.3.10")
//    implementation("org.slf4j:slf4j-api:2.0.12")
//    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}