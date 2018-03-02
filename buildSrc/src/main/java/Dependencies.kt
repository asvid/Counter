const val versionMajor = 0
const val versionMinor = 3
const val versionPatch = 3
const val versionBuild = 0

object Versions {
  val dagger = "2.12"
  val support = "27.0.2"
  val robolectric = "3.4.2"
  val kotlin = "1.2.21"
  val timber = "4.6.1"
  val rx = "2.1.9"
  val room = "1.0.0"
}

object ProjectDpes {
  val gradlePlugin = "com.android.tools.build:gradle:3.1.0-alpha01"
  val realmPlugin = "io.realm:realm-gradle-plugin:3.5.0"
  val googleServices = "com.google.gms:google-services:3.0.0"
  val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
  val kotlinExtensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}"
  val firebasePlugin = "com.google.firebase:firebase-plugins:1.0.5"
}

object Build {
  val compileSdkVersion = 27
  val minSdkVersion = 19
  val targetSdkVersion = 27
  val versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild
  val versionName = "$versionMajor.$versionMinor.$versionPatch.$versionBuild"
}

object Apt {
  val androidApi = "com.google.android:android:2.2.1"
  val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"
}

object Support {
  val appcompat = "com.android.support:appcompat-v7:${Versions.support}"
  val annotations = "com.android.support:support-annotations:${Versions.support}"
  val appCompat = "com.android.support:appcompat-v7:${Versions.support}"
  val design = "com.android.support:design:${Versions.support}"
  val constraint = "com.android.support.constraint:constraint-layout:1.0.2"
  val preference = "com.android.support:preference-v7:${Versions.support}"
  val recyclerView = "com.android.support:recyclerview-v7:${Versions.support}"
  val cardView = "com.android.support:cardview-v7:${Versions.support}"
}

object Test {
  val junit = "junit:junit:4.12"
  val mockito = "org.mockito:mockito-core:2.0.42-beta"
  val assertj = "org.assertj:assertj-core:2.6.0"
  val compileTesting = "com.google.testing.compile:compile-testing:0.11"
}

object External {
  val dagger = "com.google.dagger:dagger:${Versions.dagger}"
  val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
  val rxjava2 = "io.reactivex.rxjava2:rxjava:${Versions.rx}"
  val rxrelay2 = "com.jakewharton.rxrelay2:rxrelay:2.0.0"
  val rxbinding = "com.jakewharton.rxbinding2:rxbinding:2.0.0"
  val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre8:${Versions.kotlin}"
  val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  val charts = "com.github.PhilJay:MPAndroidChart:v3.0.3"
  val colorPicker = "com.thebluealliance:spectrum:0.7.1"
  val materialColors = "com.wada811:android-material-design-colors:3.0.0"
  val vectorIcons = "com.mikepenz:iconics-core:3.0.2@aar"
  val vectorIconsViews = "com.mikepenz:iconics-views:3.0.2@aar"
  val fontAwesomeIcons = "com.mikepenz:fontawesome-typeface:5.0.6.0@aar"
  val prettyTime = "org.ocpsoft.prettytime:prettytime:4.0.1.Final"
  val leakcanaryDebug = "com.squareup.leakcanary:leakcanary-android:1.5.4"
  val roomRuntime = "android.arch.persistence.room:runtime:${Versions.room}"
  val roomRx = "android.arch.persistence.room:rxjava2:1.0.0"
  val leakcanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:1.5.4"
}

object Firebase {
  val core = "com.google.firebase:firebase-core:11.2.0"
  val crash = "com.google.firebase:firebase-crash:11.2.0"
  val messaging = "com.google.firebase:firebase-messaging:11.2.0"
}
