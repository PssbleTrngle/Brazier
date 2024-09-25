val mod_id: String by extra
val mod_version: String by extra
val mc_version: String by extra
val release_type: String by extra
val forge_version: String by extra
val cloth_config_version: String by extra
val curseforge_project_id: String by extra
val modrinth_project_id: String by extra
val repository: String by extra
val jei_version: String by extra
val rei_version: String by extra
val create_version: String by extra
val supplementaries_version: String by extra
val moonlight_version: String by extra
val registrate_forge_version: String by extra

forge {
    enableMixins()

    dependOn(project(":common"))

    includesMod("com.tterrag.registrate:Registrate:${registrate_forge_version}")
}

dependencies {
    modCompileOnly("mezz.jei:jei-${mc_version}-forge-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${rei_version}")
    modImplementation("me.shedaniel:RoughlyEnoughItems-forge:${rei_version}")

    if(!env.isCI) {
        modRuntimeOnly("maven.modrinth:supplementaries:${supplementaries_version}")
        modRuntimeOnly("maven.modrinth:moonlight:${moonlight_version}")
    }
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}