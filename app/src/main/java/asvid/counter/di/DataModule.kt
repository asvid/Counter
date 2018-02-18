package asvid.counter.di

import android.arch.persistence.room.Room
import android.content.Context
import asvid.counter.data.room.changes.ChangesDao
import asvid.counter.data.room.changes.ChangesDatabase
import asvid.counter.data.room.changes.ChangesRepository
import asvid.counter.data.room.counter.CounterDao
import asvid.counter.data.room.counter.CounterDatabase
import asvid.counter.data.room.counter.CounterRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataModule {

  @Singleton
  @Provides
  fun counterRepository(
      counterDao: CounterDao): CounterRepository = CounterRepository().also { it.counterDao = counterDao }

  @Singleton
  @Provides
  fun counterDatabase(context: Context): CounterDatabase = Room.databaseBuilder(context,
      CounterDatabase::class.java, "Counter.db").build()

  @Singleton
  @Provides
  fun counterDao(context: Context): CounterDao = counterDatabase(context).counterDao()

  @Singleton
  @Provides
  fun changesRepository(
      changesDao: ChangesDao): ChangesRepository = ChangesRepository().also { it.changesDao = changesDao }

  @Singleton
  @Provides
  fun changesDatabase(context: Context): ChangesDatabase = Room.databaseBuilder(context,
      ChangesDatabase::class.java, "Changes.db").build()

  @Singleton
  @Provides
  fun changesDao(context: Context): ChangesDao = changesDatabase(context).changesDao()


}