package asvid.counter.di

import android.app.Activity
import asvid.counter.modules.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap


@Module
abstract class ActivityBinder {

  @Binds
  @IntoMap
  @ActivityKey(MainActivity::class)
  abstract fun bindMainActivityInjectorFactory(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>
}