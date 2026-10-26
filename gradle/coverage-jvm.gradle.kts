import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal

plugins.apply("jacoco")

val minCoveragePercent =
    (
        System.getenv("MIN_COVERAGE")
            ?: findProperty("minCoverage") as String?
            ?: "80"
    ).toBigDecimal()
val minCoverageRatio = minCoveragePercent.divide(BigDecimal("100"))

tasks.named<Test>("test") {
    finalizedBy("jacocoTestReport")
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn("test")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    dependsOn("jacocoTestReport")
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = minCoverageRatio
            }
        }
    }
    classDirectories.setFrom(
        fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/*_Factory*",
                "**/*_HiltModules*",
                "**/Hilt_*",
                "**/*Module*.class",
            )
        },
    )
    sourceDirectories.setFrom(files("src/main/kotlin", "src/main/java"))
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/test.exec", "jacoco/test*.exec")
        },
    )
}

tasks.named("check") {
    dependsOn("jacocoCoverageVerification")
}
