pluginManagement {
  repositories {
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
    google()
  }

  val korgePluginVersion: String by settings

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.soywiz.korge") {
        useModule("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
      }
    }
  }

  plugins {
    id("com.soywiz.korge") apply false
  }
}

rootProject.name = "korjector"
