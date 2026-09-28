plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))

    accessTransformer()

    dataGen()
}

dependencies {
    modInclude(libs.galena.hats.neoforge)

    modInclude(libs.registrate.neoforge)
    modInclude(libs.multikulti.core.neoforge)
    modInclude(libs.multikulti.registrate.neoforge)

    modImplementation(libs.multikulti.datagen.neoforge)

    modCompileOnly(libs.jei.common.api)
    modCompileOnly(libs.jei.neoforge.api)

    if (!env.isCI) {
        modRuntimeOnly(libs.jei.neoforge)

        modRuntimeOnly(pack.neoforge.modrinth.supplementaries)
        modRuntimeOnly(pack.neoforge.modrinth.moonlight)
        modRuntimeOnly(pack.neoforge.curseforge.configured)
    }
}

upload {
    modrinth {
        syncBodyFromReadme()
    }
}
