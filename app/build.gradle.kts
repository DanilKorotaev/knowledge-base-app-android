import java.math.BigDecimal

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    jacoco
}

val minCoveragePercent =
    (
        System.getenv("MIN_COVERAGE")
            ?: findProperty("minCoverage") as String?
            ?: "80"
    ).toBigDecimal()
val minCoverageRatio = minCoveragePercent.divide(BigDecimal("100"))

android {
    namespace = "com.coredan.knowledgebaseapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.coredan.knowledgebaseapp"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = rootProject.file("VERSION").readText().trim()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}

val jacocoExcludes =
    listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/Hilt_*.*",
        "**/*_HiltModules*.*",
        "**/*_Factory*.*",
        "**/*Module*.*",
        "**/ComposableSingletons*.*",
        "**/ui/theme/**",
        "**/ui/placeholder/PlaceholderScreen*",
        "**/MainActivity.*",
        "**/KnowledgeBaseApplication.*",
    )

fun jacocoClassDirectories(): FileTree =
    fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(jacocoExcludes)
    }

fun jacocoExecutionData(): FileTree =
    fileTree(layout.buildDirectory) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/testDebugUnitTest.exec",
        )
    }

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    group = "verification"
    description = "Generate JaCoCo coverage report for unit tests"

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(jacocoClassDirectories())
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(jacocoExecutionData())
}

tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    dependsOn("jacocoTestReport")
    group = "verification"
    description = "Fail if line coverage is below minCoverage ($minCoveragePercent%)"

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = minCoverageRatio
            }
        }
    }

    classDirectories.setFrom(jacocoClassDirectories())
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(jacocoExecutionData())
}

tasks.named("check") {
    dependsOn("jacocoCoverageVerification")
}
