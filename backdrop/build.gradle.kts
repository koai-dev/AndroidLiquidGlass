import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.plugins.signing.SigningExtension

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    id("com.vanniktech.maven.publish")
}

kotlin {
    android {
        minSdk = 21
        compileSdk = 37
        buildToolsVersion = "37.0.0"
        namespace = "com.koaidev.backdrop"
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    applyDefaultHierarchyTemplate()

    jvm("desktop")

    js {
        browser()
    }
    wasmJs {
        browser()
    }

    macosArm64()
    iosArm64("iosArm64")
    iosSimulatorArm64("iosSimulatorArm64")

    sourceSets {
        val commonMain = getByName("commonMain") {
            dependencies {
                api(libs.compose.foundation)
                api(libs.compose.ui)
                api(libs.compose.ui.graphics)
                implementation(libs.compose.animation.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kyant.shapes)
                implementation(libs.jetbrains.annotations)
            }
        }

        val androidMain = getByName("androidMain") {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val skikoMain = create("skikoMain") {
            dependsOn(commonMain)
        }

        val desktopMain = getByName("desktopMain") {
            dependsOn(skikoMain)
        }

        val macosArm64Main = getByName("macosArm64Main") {
            dependsOn(skikoMain)
        }

        val iosMain = getByName("iosMain") {
            dependsOn(skikoMain)
        }

        val iosArm64Main = getByName("iosArm64Main") {
        }

        val iosSimulatorArm64Main = getByName("iosSimulatorArm64Main") {
        }

        val jsMain = getByName("jsMain") {
            dependsOn(skikoMain)
        }

        val wasmJsMain = getByName("wasmJsMain") {
            dependsOn(skikoMain)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.koai-dev", "backdrop", "2.0.1")

    pom {
        name.set("Backdrop")
        description.set("Compose Multiplatform Liquid Glass effects")
        inceptionYear.set("2025")
        url.set("https://github.com/koai-dev/AndroidLiquidGlass")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("koai-dev")
                name.set("koai-dev")
                url.set("https://github.com/koai-dev")
            }
        }
        scm {
            url.set("https://github.com/koai-dev/AndroidLiquidGlass")
            connection.set("scm:git:git://github.com/koai-dev/AndroidLiquidGlass.git")
            developerConnection.set("scm:git:ssh://git@github.com/koai-dev/AndroidLiquidGlass.git")
        }
    }
}

// Use the local GPG keyring when configured; CI can still supply an in-memory key.
if (providers.gradleProperty("signing.gnupg.keyName").isPresent &&
    !providers.gradleProperty("signingInMemoryKey").isPresent
) {
    extensions.configure<SigningExtension> {
        useGpgCmd()
    }
}
