val mod_id: String by extra
val mod_version: String by extra
val fabric_loader_version: String by extra
val cloth_config_version: String by extra
val mc_version: String by extra
val jei_version: String by extra
val rei_version: String by extra
val registrate_fabric_version: String by extra
val forge_config_api_port_version: String by extra

plugins {
    id("dev.architectury.loom") version ("1.6-SNAPSHOT")
}

common {
    applyVanillaGradle = false
}

dependencies {
    "minecraft"("com.mojang:minecraft:${mc_version}")
    "mappings"(loom.officialMojangMappings())

    modCompileOnly("com.tterrag.registrate_fabric:Registrate:${registrate_fabric_version}")
    modCompileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common:${forge_config_api_port_version}")

    modCompileOnly("mezz.jei:jei-${mc_version}-common-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:${rei_version}")

    modCompileOnly("mezz.jei:jei-${mc_version}-common-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:${rei_version}")
}