plugins {
    java
    id("com.gradleup.shadow") version "9.2.2"
}

group = "de.c4t4lysm"
version = "3.0.0"
description = "Mines management plugin"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    implementation(platform("com.intellectualsites.bom:bom-newest:1.55")) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    implementation("org.bstats:bstats-bukkit:3.1.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks {
    processResources {
        filteringCharset = "UTF-8"

        filesMatching("plugin.yml") {
            expand(
                "pluginName" to "CataMines",
                "version" to project.version,
                "description" to project.description,
                "main" to "me.catalysmrl.catamines.CataMines",
            )
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        exclude("**/legacycatamines/**")
    }

    shadowJar {
        dependsOn(processResources)

        archiveBaseName.set("catamines")
        archiveVersion.set(project.version.toString())

        relocate("org.bstats", "me.catalysmrl.catamines.shaded.metrics")

        from(processResources) {
            include("plugin.yml")
        }

        from(processResources) {
            exclude("plugin.yml")
        }
    }

    jar {
        enabled = false // disable normal jar
    }

    build {
        dependsOn(shadowJar)
    }
}
