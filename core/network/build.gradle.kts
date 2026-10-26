plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(project(":core:model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}

tasks.test {
    useJUnitPlatform()
}

apply(from = rootProject.file("gradle/coverage-jvm.gradle.kts"))
