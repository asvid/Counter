package asvid.counter.data.room.counter

import android.arch.persistence.room.Entity
import asvid.counter.data.room.counter.CounterEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class CounterEntity {

  companion object {
    const val TABLE_NAME = "counter"
  }

}