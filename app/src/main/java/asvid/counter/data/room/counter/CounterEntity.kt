package asvid.counter.data.room.counter

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import asvid.counter.data.room.counter.CounterEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CounterEntity(var name: String?, var value: Int?) {

  companion object {
    const val TABLE_NAME = "counters"
    const val ID = "id"
  }

  @PrimaryKey
  @NonNull
  var id: Long? = null

}