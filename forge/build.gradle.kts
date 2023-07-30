import net.darkhax.curseforgegradle.TaskPublishCurseForge
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.possible_triangle.gradle.loadEnv
import org.gradle.api.component.AdhocComponentWithVariants
import net.fabricmc.loom.task.RemapJarTask

val mod_id: String by extra
val mod_version: String by extra
val minecraft_version: String by extra
val release_type: String by extra
val forge_version: String by extra
val architectury_version: String by extra
val cloth_config_version: String by extra
val curseforge_project_id: String by extra
val modrinth_project_id: String by extra
val repository: String by extra
val jei_version: String by extra
val rei_version: String by extra
val create_version: String by extra
val supplementaries_version: String by extra
val moonlight_version: String by extra

val env = loadEnv()

plugins {
    id("com.github.johnrengelman.shadow")
    id("net.darkhax.curseforgegradle")
    id("com.modrinth.minotaur")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge {
        mixinConfig("${mod_id}-common.mixins.json")
    }

    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

val common by configurations.creating
val shadowCommon by configurations.creating

configurations["compileClasspath"].extendsFrom(common)
configurations["runtimeClasspath"].extendsFrom(common)
configurations["developmentForge"].extendsFrom(common)

dependencies {
    forge("net.minecraftforge:forge:${minecraft_version}-${forge_version}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-forge:${architectury_version}")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }

    modImplementation("me.shedaniel.cloth:cloth-config-forge:${cloth_config_version}")

    modCompileOnly("mezz.jei:jei-${minecraft_version}-forge-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${rei_version}")
    modImplementation("me.shedaniel:RoughlyEnoughItems-forge:${rei_version}")

    if(!env.isCI) {
        //modRuntimeOnly("mezz.jei:jei-${minecraft_version}-forge:${jei_version}")
        //modRuntimeOnly("maven.modrinth:create:${create_version}")
        modRuntimeOnly("maven.modrinth:supplementaries:${supplementaries_version}")
        modRuntimeOnly("maven.modrinth:moonlight:${moonlight_version}")
    }
}

tasks.withType<ShadowJar> {
    exclude("fabric.mod.json")
    exclude("architectury.common.json")

    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask> {
    val shadowTask = tasks.shadowJar.get()
    input.set(shadowTask.archiveFile)
    dependsOn(shadowTask)
    archiveClassifier.set("")
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.sourcesJar.get()
    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
}

components.getByName<AdhocComponentWithVariants>("java").apply {
    withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}

env["CURSEFORGE_TOKEN"]?.let { token ->
    tasks.register<TaskPublishCurseForge>("curseforge") {
        dependsOn(tasks.jar)

        apiToken = token

        upload(curseforge_project_id, tasks.remapJar.get().archiveFile).apply {
            changelogType = "html"
            changelog = env["CHANGELOG"]
            releaseType = release_type
            addModLoader("Forge")
            addGameVersion(minecraft_version)
            displayName = "Forge $mod_version"

            addRelation("architectury-api", "requiredDependency")
            addRelation("cloth-config", "requiredDependency")
        }
    }
}

env["MODRINTH_TOKEN"]?.let { modrinthToken ->
    modrinth {
        token.set(modrinthToken)
        projectId.set(modrinth_project_id)
        versionNumber.set(mod_version)
        versionName.set("Forge $mod_version")
        changelog.set(env["CHANGELOG"])
        gameVersions.set(listOf(minecraft_version))
        loaders.set(listOf("forge"))
        versionType.set(release_type)
        uploadFile.set(tasks.remapJar.get())
        dependencies {
            required.project("lhGA9TYQ")
            required.project("9s6osm5g")
        }
    }
}