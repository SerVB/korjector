plugins {
  id("com.soywiz.korge")
}

korge {
  targetJvm()
  targetJs()
  targetIos()
  targetAndroidIndirect()
  targetDesktop()

  serializationJson()
  useNewMemoryModel = true
}

kotlin {
  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
    }
  }
}
