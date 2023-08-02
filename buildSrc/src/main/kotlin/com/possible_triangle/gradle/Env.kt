package com.possible_triangle.gradle

import org.gradle.api.Project

class Env(private val values: Map<String, String>) {
    val isCI = values["CI"] == "true"

    operator fun get(key: String) = values[key]
}

fun Project.loadEnv(): Env {
    val localEnv = file(".env").takeIf { it.exists() }?.readLines()?.associate {
        val (key, value) = it.split("=")
        key to value
    } ?: emptyMap()

    return Env(System.getenv() + localEnv)
}