val mod_id: String by extra
val mod_version: String by extra
val mc_version: String by extra

plugins {
    id("com.possible-triangle.architectury")
}

dependencies {
    modCompileOnly(libs.registrate.fabric)
    modCompileOnly(libs.multikulti.registrate.fabric)
    modCompileOnly(libs.forge.config.api.common)

    modCompileOnly(libs.jei.common.api)
}