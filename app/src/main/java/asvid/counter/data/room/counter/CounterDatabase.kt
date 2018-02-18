package asvid.counter.data.room.counter

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [CounterEntity::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {

  abstract fun counterDao(): CounterDao
}