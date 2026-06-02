pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KinminTracker"

include(":app")

include(
    ":core:common",
    ":core:model",
    ":core:designsystem",
    ":core:navigation",
    ":core:network",
    ":core:database",
    ":core:datastore",
    ":core:sync",
    ":core:location",
    ":core:camera"
)

include(
    ":feature:auth",
    ":feature:home",
    ":feature:order",
    ":feature:activity",
    ":feature:remarks",
    ":feature:summary",
    ":feature:parties",
    ":feature:help"
)
