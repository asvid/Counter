const val versionMajor = 0
const val versionMinor = 3
const val versionPatch = 3
const val versionBuild = 0

object Versions {
  const val dagger = "2.12"
  const val support = "27.0.2"
  const val robolectric = "3.4.2"
  const val kotlin = "1.2.21"
  const val timber = "4.6.1"
  const val rx = "2.1.9"
  const val room = "1.0.0"
}

object ProjectDpes {
  const val gradlePlugin = "com.android.tools.build:gradle:3.1.0-alpha01"
  const val realmPlugin = "io.realm:realm-gradle-plugin:3.5.0"
  const val googleServices = "com.google.gms:google-services:3.0.0"
  const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
  const val kotlinExtensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}"
  const val firebasePlugin = "com.google.firebase:firebase-plugins:1.0.5"
}

object Build {
  const val compileSdkVersion = 27
  const val minSdkVersion = 19
  const val targetSdkVersion = 27
  const val versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild
  const val versionName = "$versionMajor.$versionMinor.$versionPatch.$versionBuild"
}

object Apt {
  const val androidApi = "com.google.android:android:2.2.1"
  const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"
}

object Support {
  const val appcompat = "com.android.support:appcompat-v7:${Versions.support}"
  const val annotations = "com.android.support:support-annotations:${Versions.support}"
  const val appCompat = "com.android.support:appcompat-v7:${Versions.support}"
  const val design = "com.android.support:design:${Versions.support}"
  const val constraint = "com.android.support.constraint:constraint-layout:1.0.2"
  const val preference = "com.android.support:preference-v7:${Versions.support}"
  const val recyclerView = "com.android.support:recyclerview-v7:${Versions.support}"
  const val cardView = "com.android.support:cardview-v7:${Versions.support}"
}

object Test {
  const val junit = "junit:junit:4.12"
  const val mockito = "org.mockito:mockito-core:2.0.42-beta"
  const val assertj = "org.assertj:assertj-core:2.6.0"
  const val compileTesting = "com.google.testing.compile:compile-testing:0.11"
}

object External {
  const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
  const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
  const val rxjava2 = "io.reactivex.rxjava2:rxjava:${Versions.rx}"
  const val rxrelay2 = "com.jakewharton.rxrelay2:rxrelay:2.0.0"
  const val rxbinding = "com.jakewharton.rxbinding2:rxbinding:2.0.0"
  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre8:${Versions.kotlin}"
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  const val charts = "com.github.PhilJay:MPAndroidChart:v3.0.3"
  const val colorPicker = "com.thebluealliance:spectrum:0.7.1"
  const val materialColors = "com.wada811:android-material-design-colors:3.0.0"
  const val vectorIcons = "com.mikepenz:iconics-core:3.0.2@aar"
  const val vectorIconsViews = "com.mikepenz:iconics-views:3.0.2@aar"
  const val fontAwesomeIcons = "com.mikepenz:fontawesome-typeface:5.0.6.0@aar"
  const val prettyTime = "org.ocpsoft.prettytime:prettytime:4.0.1.Final"
  const val leakcanaryDebug = "com.squareup.leakcanary:leakcanary-android:1.5.4"
  const val roomRuntime = "android.arch.persistence.room:runtime:${Versions.room}"
  const val roomRx = "android.arch.persistence.room:rxjava2:1.0.0"
  const val leakcanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:1.5.4"
}

object Firebase {
  const val core = "com.google.firebase:firebase-core:11.2.0"
  const val crash = "com.google.firebase:firebase-crash:11.2.0"
  const val messaging = "com.google.firebase:firebase-messaging:11.2.0"
}
