package asvid.counter.data.room.counter

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import asvid.counter.data.room.counter.changes.ChangesDao
import asvid.counter.data.room.counter.changes.ChangesEntity

@Database(entities = [CounterEntity::class, ChangesEntity::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {

  abstract fun counterDao(): CounterDao
  abstract fun changesDao(): ChangesDao
}