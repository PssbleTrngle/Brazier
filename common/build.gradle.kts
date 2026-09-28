plugins {
    id("com.possible-triangle.common")
}

dependencies {
    modCompileOnly(libs.registrate.neoforge)
    modCompileOnly(libs.multikulti.registrate.common)
    accessTransformers(libs.multikulti.core.common)

    modCompileOnly(libs.config.api.port.common)

    modCompileOnly(libs.jei.common.api)
}
