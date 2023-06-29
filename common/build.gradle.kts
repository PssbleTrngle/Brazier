val enabled_platforms: String by extra
val mod_id: String by extra
val mod_version: String by extra
val fabric_loader_version: String by extra
val architectury_version: String by extra
val cloth_config_version: String by extra

architectury {
    common(enabled_platforms.split(","))
}

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${architectury_version}")

    // Cloth Config
    modCompileOnly("me.shedaniel.cloth:cloth-config:${cloth_config_version}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/${mod_id}.accesswidener"))
}