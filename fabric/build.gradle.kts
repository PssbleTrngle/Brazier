val mc_version: String by extra

plugins {
    id("com.possible-triangle.fabric")
}

mod {
    mods.include(libs.registrate.fabric)
    mods.include(libs.multikulti.core.fabric)
    mods.include(libs.multikulti.registrate.fabric)
    mods.include(libs.forge.config.api.fabric)
    mods.include(libs.galena.hats.fabric)
}

fabric {
    dependOn(project(":common"))

    dataGen()
}

loom {
    accessWidenerPath = file("src/main/resources/${mod.id.get()}.accesswidener")
}

dependencies {
    modCompileOnly(libs.jei.common.api)
    modCompileOnly(libs.jei.fabric.api)

    modImplementation(libs.multikulti.datagen.fabric)

    if (!env.isCI) {
        modRuntimeOnly(libs.jei.fabric)

        modRuntimeOnly(pack.fabric.modrinth.supplementaries)
        modRuntimeOnly(pack.fabric.modrinth.moonlight)
    }
}