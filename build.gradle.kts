import org.gradle.kotlin.dsl.korge

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
}

kotlin {
  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
    }
  }
}

tasks {
  val runJvm by getting(com.soywiz.korge.gradle.targets.jvm.KorgeJavaExec::class)
  create("runJvmNativeImage", com.soywiz.korge.gradle.targets.jvm.KorgeJavaExec::class) {
    group = com.soywiz.korge.gradle.targets.GROUP_KORGE_RUN
    dependsOn("jvmMainClasses")
    mainClass.set(runJvm.mainClass)
    environment("USE_NATIVE_IMAGE", "true")
  }
}
