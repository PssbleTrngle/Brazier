import net.minecraftforge.gradle.common.util.MinecraftExtension

val mc_version: String by extra
val jei_version: String by extra
val rei_version: String by extra
val supplementaries_version: String by extra
val moonlight_version: String by extra
val registrate_forge_version: String by extra

forge {
    enableMixins()

    dependOn(project(":common"))

    includesMod("com.tterrag.registrate:Registrate:${registrate_forge_version}")
}

configure<MinecraftExtension> {
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
}

dependencies {
    modCompileOnly("mezz.jei:jei-${mc_version}-forge-api:${jei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${rei_version}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-forge:${rei_version}")

    if(!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")
        modRuntimeOnly("maven.modrinth:supplementaries:${supplementaries_version}")
        modRuntimeOnly("maven.modrinth:moonlight:${moonlight_version}")
    }
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}