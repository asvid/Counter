package asvid.counter.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import asvid.counter.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  internal fun context(application: App): Context {
    return application.applicationContext
  }

  @Provides
  @Singleton
  internal fun sharedPreferences(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
  }
}
