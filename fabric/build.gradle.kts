val mc_version: String by extra
val jei_version: String by extra
val rei_version: String by extra
val registrate_fabric_version: String by extra
val forge_config_api_port_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":common"))

    dataGen()

    includesMod("com.tterrag.registrate_fabric:Registrate:${registrate_fabric_version}")
    includesMod("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${forge_config_api_port_version}")
}

dependencies {
    modCompileOnly("mezz.jei:jei-${mc_version}-fabric-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${rei_version}")

    if(!env.isCI) {
        // modRuntimeOnly("mezz.jei:jei-${mc_version}-fabric:${jei_version}")
        modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:${rei_version}")
    }
}

uploadToCurseforge()
uploadToModrinth()