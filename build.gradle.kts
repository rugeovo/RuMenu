import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.22"
    kotlin("jvm") version "2.1.10"
}

taboolib {
    env {
        // 安装模块
        install(Basic,
            Bukkit,
            BukkitHook,
            BukkitUtil,
            BukkitUI,
            Kether,
            Database,
            PtcObject,
            JavaScript
        )
    }
    version {
        taboolib = "6.2.3-0b616a8"
        coroutines = "1.10.1"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
    compileOnly("ink.ptms.core:v12104:12104:universal@jar")
    compileOnly("ink.ptms.core:v12104:12104:mapped@jar")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
