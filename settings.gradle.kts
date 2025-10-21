pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    id("com.possible-triangle.helper") version ("1.0.57")
    id("com.possible-triangle.packwiz") version ("1.0.57")
}

packwiz {
    packs.create("forge") {
        from = file("pack/forge")
    }

    packs.create("fabric") {
        from = file("pack/fabric")
    }
}

include("common", "fabric", "forge")
