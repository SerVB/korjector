plugins {
  id("com.soywiz.korge")
}

korge {
  targetJvm()
  targetJs()
  targetIos()
  targetAndroidIndirect()

  serializationJson()
}

kotlin {
  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
    }
  }
}
