import com.vanniktech.maven.publish.SonatypeHost
import java.net.URI

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("com.vanniktech.maven.publish") version "0.31.0"
}

android {
    namespace = "com.spotly.onboarding"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}
mavenPublishing {
    coordinates(
        groupId = "io.github.bahar-developer",
        artifactId = "spotly-onboarding",
        version = "1.0.0"
    )

    pom {
        name.set("Spotly Onboarding")
        description.set("A dynamic Jetpack Compose onboarding spotlight library with morphing animations.")
        url.set("https://github.com/Bahar-Developer/spotly-onboarding")

        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("bahar-developer")
                name.set("Bahar")
                email.set("mahboobeRezaee68@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:github.com/Bahar-Developer/spotly-onboarding.git")
            developerConnection.set("scm:git:ssh://github.com/Bahar-Developer/spotly-onboarding.git")
            url.set("https://github.com/Bahar-Developer/spotly-onboarding/tree/main")
        }
    }

    // Fix Windows file URI issue in Maven publishing
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

afterEvaluate {
    publishing {
        repositories {
            findByName("mavenCentral")?.let { repo ->
                if (repo is MavenArtifactRepository) {
                    runCatching {
                        val field = repo::class.java.superclass.getDeclaredField("urlArtifactRepository")
                        field.isAccessible = true
                        val urlRepo = field.get(repo)
                        if (urlRepo != null) {
                            val urlField = urlRepo::class.java.getDeclaredField("url")
                            urlField.isAccessible = true
                            @Suppress("UNCHECKED_CAST")
                            val provider = urlField.get(urlRepo) as? Provider<Any>
                            if (provider != null) {
                                val fixedProvider = provider.map { raw ->
                                    val str = raw.toString()
                                    if (str.contains('\\')) {
                                        val cleanPath = str.removePrefix("file://").replace('\\', '/')
                                        URI("file:///$cleanPath")
                                    } else {
                                        raw
                                    }
                                }
                                repo.setUrl(fixedProvider)
                            }
                        }
                    }
                }
            }
        }
    }
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}