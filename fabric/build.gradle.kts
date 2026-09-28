plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))

    accessWidener()
}

dependencies {
    modInclude(libs.galena.hats.fabric)

    modInclude(libs.registrate.fabric)
    modInclude(libs.multikulti.core.fabric)
    modInclude(libs.multikulti.registrate.fabric)

    modInclude(libs.config.api.port.fabric)

    modCompileOnly(libs.jei.common.api)
    modCompileOnly(libs.jei.fabric.api)

    if (!env.isCI) {
        modRuntimeOnly(libs.jei.fabric)

        modRuntimeOnly(pack.fabric.modrinth.supplementaries)
        modRuntimeOnly(pack.fabric.modrinth.moonlight)
    }
}
