plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "ru.sbt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.eclipse.paho:org.eclipse.paho.mqttv5.client:1.2.5")
    implementation("com.google.protobuf:protobuf-java:3.17.3")
    implementation("org.slf4j:slf4j-api:1.7.30")



}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}