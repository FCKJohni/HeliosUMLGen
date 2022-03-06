plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "dev.heliosteam"
version = "1.0-SNAPSHOT"


application {
    mainClass.set("dev.heliosteam.heliosumlgen.HeliosUMLGen")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("guru.nidi:graphviz-java:0.18.1")

    implementation("org.jetbrains:annotations:22.0.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")


    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")

    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.0")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("org.apache.xmlgraphics:batik-all:1.14")

}