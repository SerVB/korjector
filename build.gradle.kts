plugins {
  id("com.soywiz.korge")
}

korge {
  targetJvm()
  targetJs()
}

kotlin {
  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
    }
  }
}
