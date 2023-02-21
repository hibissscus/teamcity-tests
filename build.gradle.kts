import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.20-Beta"
    id("com.avast.gradle.docker-compose") version ("0.16.11")
    id("io.qameta.allure") version ("2.11.2")
}

kotlin {
    jvmToolchain(17)
}

group = "teamcity-tests"
version = "1.0.0"
description = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://packages.jetbrains.team/maven/p/teamcity-rest-client/teamcity-rest-client") }
}

val testngVersion = "6.11.2"
val selenideVersion = "6.11.2"
val allureVersion = "2.21.0"

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    // selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.8.1")
    // testee
    implementation("com.github.hibissscus:testee:1.6.8")
    // reportng
    implementation("com.github.hibissscus:reportng:1.5.3")
    // teamcity-rest-client
    implementation("org.jetbrains.teamcity:teamcity-rest-client:1.18.0")
    // slf4j
    implementation("org.slf4j:slf4j-api:1.7.36")

    // General testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.testng:testng:$testngVersion")
    testImplementation("org.slf4j:slf4j-log4j12:1.7.36")
    testImplementation("org.assertj:assertj-core:3.24.2")

    // UI tests
    testImplementation("com.codeborne:selenide:$selenideVersion")
    testImplementation("com.codeborne:selenide-testng:$testngVersion")

    // Reporting
    testImplementation("io.qameta.allure:allure-testng:$allureVersion")
    testImplementation("io.qameta.allure:allure-java-commons:${allureVersion}")
    testImplementation("io.qameta.allure:allure-attachments:${allureVersion}")
    testImplementation("io.qameta.allure:allure-generator:${allureVersion}")
    testImplementation("io.qameta.allure:allure-httpclient:${allureVersion}")
    testImplementation("io.qameta.allure:allure-selenide:$allureVersion")
}


fun Test.testNG(desc: String, suite: String, reportngTitle: String = "test") {
    description = desc
    group = "verification"
    ignoreFailures = true
    outputs.upToDateWhen { false }
    testLogging.showStandardStreams = true
    reports.html.required.set(false)
    reports.junitXml.required.set(false)
    useTestNG {
        suites(suite)
        useDefaultListeners = false
        listeners = setOf("testee.it.reportng.HTMLReporter")
        systemProperties = mapOf(
            "testee.it.reportng.title" to reportngTitle,
            "allure.results.directory" to project.buildDir.toString() + "/allure-results"
        )
    }
}

tasks.withType(Test::class) {
    testNG("run entire test suite locally", "src/test/resources/local.xml", "teamcity-tests-local")
}

tasks.register<Test>("docker") {
    testNG("run entire test suite locally", "src/test/resources/docker.xml", "teamcity-tests-docker")
}

allure {
    version.set(allureVersion)
    adapter {
        autoconfigure.set(false)
        aspectjWeaver.set(true)
        frameworks {
            testng {
                enabled.set(true)
                autoconfigureListeners.set(true)
            }
        }
    }
}

dockerCompose {
    useComposeFiles.add(
        if (OperatingSystem.current() != null && OperatingSystem.current().toString().contains("aarch64"))
            "docker-compose-arm.yml" else "docker-compose.yml"
    )
    stopContainers.set(false)
    isRequiredBy(tasks.getByName("docker"))
}