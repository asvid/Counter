package asvid.counter.di

import asvid.counter.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  ActivityBinder::class,
  AppModule::class,
  DataModule::class])
interface AppComponent {

  fun inject(app: App)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(app: App): Builder

    fun build(): AppComponent
  }
}