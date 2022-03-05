import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import shadow.org.apache.tools.zip.ZipEntry
import shadow.org.apache.tools.zip.ZipOutputStream

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
    implementation("guru.nidi:graphviz-java:0.18.1")
    implementation("org.graalvm.js:js:22.0.0")

    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")

    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.0")
}

