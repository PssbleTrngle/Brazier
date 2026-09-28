plugins {
    id("com.possible-triangle.common")
}

dependencies {
    modCompileOnly(libs.registrate.neoforge)
    modCompileOnlyApi(libs.multikulti.registrate.common)
    accessTransformers(libs.multikulti.core.common)

    modCompileOnly(libs.config.api.port.common)

    modCompileOnlyApi(libs.jei.common.api)
}
