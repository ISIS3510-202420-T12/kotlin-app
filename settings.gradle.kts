import java.util.Properties

// Load local.properties file
val localProperties = Properties()
val localPropertiesFile = rootDir.resolve("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

// Get the MapBox token from local.properties
val mapboxToken = localProperties.getProperty("MAPBOX_DOWNLOADS_TOKEN")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
            // Use the secret token
            credentials.password = mapboxToken
            
            authentication.create<BasicAuthentication>("basic")
        }
    }
}

rootProject.name = "WearAbouts"
include(":app")
 