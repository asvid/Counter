package asvid.counter

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import asvid.counter.di.DaggerAppComponent
import asvid.counter.di.Di
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/**
 * Created by adam on 15.01.17.
 */

class App : Application(), HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun activityInjector(): AndroidInjector<Activity> {
    return dispatchingAndroidInjector
  }

  override fun onCreate() {
    super.onCreate()
    init()
  }

  private fun init() {
    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this)

    initLeakCanary()
    initTimber()
    Di.set(this)
//    setCrashReporting()
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
  }

  private fun setCrashReporting() {
    if (BuildConfig.DEBUG) {
      Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
        Timber.wtf("Alert", throwable.message, throwable)
        System.exit(2)
      }
    }
  }

  private fun initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }

  private fun initLeakCanary() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }
    LeakCanary.install(this)
  }
}
