plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version (libs.versions.shadow)
    id("com.diffplug.spotless") version (libs.versions.spotless)
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = if (project == rootProject) "net.skyplex" else "net.skyplex.SkyCommon"
    version = System.getenv("GITHUB_SHA")?.subSequence(0, 8) ?: "dev-SNAPSHOT"

    java {
        withSourcesJar()
        withJavadocJar()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    if (name != "event") {
        apply(plugin = "com.diffplug.spotless")
        spotless {
            java {
                licenseHeaderFile(rootProject.file("HEADER.txt"))
                removeUnusedImports()
                indentWithSpaces(4)
            }
        }
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        options.encoding = "UTF-8"
    }
    tasks.withType(Javadoc::class.java).configureEach {
        options.encoding = "UTF-8"
    }

    configurations.configureEach {
        resolutionStrategy.cacheChangingModulesFor(10, "minutes")
    }

    repositories {
        maven {
            name = "SkyPlex"
            credentials(PasswordCredentials::class.java)
            url = uri("https://repo.mc-skyplex.net/releases")
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.aikar.co/content/groups/aikar/") }
    }

    dependencies {
        testImplementation(rootProject.libs.junit)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])

                repositories.add(project.repositories["SkyPlex"])
            }
        }
    }
}

dependencies {
    subprojects.forEach(this::api)
}
