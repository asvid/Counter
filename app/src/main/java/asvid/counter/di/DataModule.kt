package asvid.counter.di

import android.arch.persistence.room.Room
import android.content.Context
import asvid.counter.data.room.counter.CounterDao
import asvid.counter.data.room.counter.CounterDatabase
import asvid.counter.data.room.counter.CounterRepository
import asvid.counter.data.room.counter.changes.ChangesDao
import asvid.counter.data.room.counter.changes.ChangesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataModule {

  @Singleton
  @Provides
  fun counterDatabase(context: Context): CounterDatabase = Room.databaseBuilder(context,
      CounterDatabase::class.java, "Counter.db").allowMainThreadQueries().build()

  @Singleton
  @Provides
  fun counterRepository(counterDao: CounterDao): CounterRepository = CounterRepository(counterDao)

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
  @Named("string1")
  fun provideString1() = "STRING_1"

}