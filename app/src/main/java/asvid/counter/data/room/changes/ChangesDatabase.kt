package asvid.counter.data.room.changes

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [ChangesEntity::class], version = 1)
abstract class ChangesDatabase : RoomDatabase() {

  abstract fun changesDao(): ChangesDao
}