package asvid.counter.di

import asvid.counter.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
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