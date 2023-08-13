import com.possible_triangle.gradle.loadEnv
import net.fabricmc.loom.api.LoomGradleExtensionAPI

val mod_version: String by extra
val mod_name: String by extra
val mod_id: String by extra
val mod_author: String by extra
val minecraft_version: String by extra
val repository: String by extra
val maven_group: String by extra
val enabled_platforms: String by extra

val env = loadEnv()

plugins {
    java
    id("maven-publish")
    id("architectury-plugin") version ("3.4-SNAPSHOT")
    id("dev.architectury.loom") version ("1.3-SNAPSHOT") apply (false)
    id("org.sonarqube") version ("4.3.0.3225")
    id("net.darkhax.curseforgegradle") version ("1.0.8") apply (false)
    id("com.modrinth.minotaur") version ("2.+") apply (false)
    id("com.github.johnrengelman.shadow") version ("7.1.2") apply (false)
}

architectury {
    minecraft = minecraft_version
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "maven-publish")

    base {
        archivesName.set("$mod_id-${project.name}")
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:${minecraft_version}")
        // The following line declares the mojmap mappings, you may use other mappings as well
        "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())
        // The following line declares the yarn mappings you may select this one as well.
        // mappings "net.fabricmc:yarn:1.16.5+build.4"
    }

    tasks.withType<ProcessResources> {
        // this will ensure that this task is redone when the versions change.
        inputs.property("version", version)

        filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "fabric.mod.json", "${mod_id}.mixins.json")) {
            expand(
                mapOf(
                    "version" to mod_version,
                    "mod_name" to mod_name,
                    "mod_id" to mod_id,
                    "mod_author" to mod_author,
                    "repository" to repository,
                )
            )
        }
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${repository}")
                version = mod_version
                credentials {
                    username = env["GITHUB_ACTOR"]
                    password = env["GITHUB_TOKEN"]
                }
            }
        }
        publications {
            create<MavenPublication>("gpr") {
                groupId = maven_group
                artifactId = "${mod_id}-${project.name}"
                version = mod_version
                from(components["java"])
            }
        }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")

    repositories {
        maven {
            url = uri("https://maven.blamejared.com/")
            content {
                includeGroup("mezz.jei")
            }
        }
        maven {
            url = uri("https://api.modrinth.com/maven")
            content {
                includeGroup("maven.modrinth")
            }
        }
        maven {
            url = uri("https://maven.shedaniel.me/")
            content {
                includeGroup("me.shedaniel")
            }
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withSourcesJar()
    }
}

sonarqube {
    properties {
        property("sonar.projectVersion", "${minecraft_version}-${mod_version}")
        property("sonar.projectKey", mod_id)
    }
}

subprojects {
    sonarqube {
        properties {
            property("sonar.sources", this@subprojects.file("src/main"))
        }
    }
}
