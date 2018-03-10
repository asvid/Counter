package asvid.counter.data.room.counter

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import asvid.counter.data.room.counter.changes.ChangesDao
import asvid.counter.data.room.counter.changes.ChangesEntity
import asvid.counter.data.room.widget.counter.CounterWidgetDao
import asvid.counter.data.room.widget.counter.CounterWidgetEntity
import asvid.counter.data.room.widget.downcounter.DownCounterWidgetDao
import asvid.counter.data.room.widget.downcounter.DownCounterWidgetEntity

@Database(entities = [
  CounterEntity::class,
  ChangesEntity::class,
  CounterWidgetEntity::class,
  DownCounterWidgetEntity::class],
    version = 1)
abstract class CounterDatabase : RoomDatabase() {

  abstract fun counterDao(): CounterDao
  abstract fun changesDao(): ChangesDao
  abstract fun counterWidgetDao(): CounterWidgetDao
  abstract fun downCounterWidgetDao(): DownCounterWidgetDao
}