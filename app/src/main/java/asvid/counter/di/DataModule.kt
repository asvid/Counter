package asvid.counter.di

import android.arch.persistence.room.Room
import android.content.Context
import asvid.counter.data.room.counter.CounterDao
import asvid.counter.data.room.counter.CounterDatabase
import asvid.counter.repository.CounterRepository
import asvid.counter.data.room.counter.changes.ChangesDao
import asvid.counter.repository.ChangesRepository
import asvid.counter.data.room.widget.counter.CounterWidgetDao
import asvid.counter.repository.CounterWidgetRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataModule {

  @Singleton
  @Provides
  fun counterDatabase(context: Context): CounterDatabase = Room.databaseBuilder(context,
      CounterDatabase::class.java, "Counter.db").allowMainThreadQueries().build()

  @Singleton
  @Provides
  fun counterRepository(counterDao: CounterDao): CounterRepository = CounterRepository(
      counterDao)

  @Singleton
  @Provides
  fun counterDao(context: Context): CounterDao = counterDatabase(context).counterDao()

  @Singleton
  @Provides
  fun changesRepository(
      changesDao: ChangesDao): ChangesRepository = ChangesRepository()

  @Singleton
  @Provides
  fun changesDao(context: Context): ChangesDao = counterDatabase(context).changesDao()

  @Singleton
  @Provides
  fun widgetRepository(counterWidgetDao: CounterWidgetDao): CounterWidgetRepository = CounterWidgetRepository(
      counterWidgetDao)

  @Singleton
  @Provides
  fun widgetDao(context: Context): CounterWidgetDao = counterDatabase(context).counterWidgetDao()

}