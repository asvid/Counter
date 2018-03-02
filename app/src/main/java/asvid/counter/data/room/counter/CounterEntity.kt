package asvid.counter.data.room.counter

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import asvid.counter.data.counter.Change
import asvid.counter.data.room.counter.CounterEntity.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME)
data class CounterEntity(var name: String, var value: Int) {

  companion object {
    const val TABLE_NAME = "counters"
    const val ID = "id"
  }

  @PrimaryKey
  @NonNull
  var id: Long? = null

  //  TODO: move to model, Entity should be pure DB class
  fun incrementValue() {
    val change = Change()
    change.date = Date()
    change.preValue = value
    value += 1
    change.postValue = value
    TODO("add change")
  }

  //  TODO: move to model, Entity should be pure DB class
  fun decrementValue() {
    val change = Change()
    change.date = Date()
    change.preValue = value
    value -= 1
    change.postValue = value
    TODO("add change")
  }

}