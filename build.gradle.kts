plugins {
    java
}

group = "eu.heliosteam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.2")
    implementation("com.google.code.gson:gson:2.9.0")
}
