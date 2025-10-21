val mc_version: String by extra
val supplementaries_version: String by extra
val moonlight_version: String by extra

plugins {
    id("com.possible-triangle.forge")
}

withKotlin()

mod {
    mods.include(libs.registrate.forge)
    mods.include(libs.multikulti.core.forge)
    mods.include(libs.multikulti.registrate.forge)
    mods.include(libs.multikulti.datagen.fix)
    mods.include(libs.galena.hats.forge)
}

forge {
    enableMixins()

    kotlinForgeVersion = "4.11.0"

    dependOn(project(":common"))
}

minecraft {
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
}

dependencies {
    modCompileOnly(libs.jei.common.api)
    modCompileOnly(libs.jei.forge.api)

    if (!env.isCI) {
        modRuntimeOnly(libs.jei.forge)

        modRuntimeOnly(pack.forge.modrinth.supplementaries)
        modRuntimeOnly(pack.forge.modrinth.moonlight)
        modRuntimeOnly(pack.forge.curseforge.configured)
    }
}

upload {
    modrinth {
        syncBodyFromReadme()
    }
}