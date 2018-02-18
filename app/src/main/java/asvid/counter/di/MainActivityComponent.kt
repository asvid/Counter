package asvid.counter.di

import asvid.counter.modules.main.MainActivity
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MainActivityComponent.MainActivityModule::class])
interface MainActivityComponent : AndroidInjector<MainActivity> {

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<MainActivity>()

  @Module
  abstract class MainActivityModule

}