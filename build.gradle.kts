import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "eu.heliosteam"
version = "1.0-SNAPSHOT"


application {
    mainClass.set("eu.heliosteam.heliosumlgen.HeliosUMLGen")
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("commons-cli:commons-cli:1.5.0")
}
