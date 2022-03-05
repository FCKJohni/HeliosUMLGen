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

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        transform(GraalTransformer())
    }
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

class GraalTransformer : com.github.jengelman.gradle.plugins.shadow.transformers.Transformer {

    @Internal
    var patternSet: PatternSet = PatternSet().include("META-INF/truffle/language")
    @Internal
    var matchCount: Int = 0
    @Internal
    var result: StringBuilder = StringBuilder()
    @Internal
    var targetPath: String = ""

    override fun getName(): String {
        return "GraalTransformer"
    }

    override fun canTransformResource(element: FileTreeElement?): Boolean {
        return patternSet.asSpec.isSatisfiedBy(element)
    }

    override fun transform(p0: TransformerContext?) {
        val context: TransformerContext = p0!!
        matchCount += 1
        targetPath = context.path
        val lines = context.`is`.bufferedReader().readLines().toMutableList()
        lines.onEachIndexed  { index, s ->
            run {
                val replacement = lines[index].replace("language1", "language$matchCount")
                lines[index] = replacement
                result.append(replacement + "\n")
            }
        }
    }

    override fun hasTransformedResource(): Boolean { return matchCount > 0 }

    override fun modifyOutputStream(p0: ZipOutputStream?, p1: Boolean) {
        val outputStream: ZipOutputStream = p0!!
        val entry: ZipEntry = ZipEntry(targetPath)
        entry.time = TransformerContext.getEntryTimestamp(p1, entry.time)
        outputStream.putNextEntry(entry)
        outputStream.write(result.toString().toByteArray())
        outputStream.closeEntry()
    }


}
