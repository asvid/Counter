package asvid.counter.data.room.counter.changes

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.data.room.counter.CounterEntity.Companion.ID
import asvid.counter.data.room.counter.changes.ChangesEntity.Companion.COUNTER_ID
import asvid.counter.data.room.counter.changes.ChangesEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME,
    foreignKeys = (arrayOf(ForeignKey(
        entity = CounterEntity::class,
        parentColumns = [ID],
        childColumns = [COUNTER_ID],
        onDelete = CASCADE))))
data class ChangesEntity(
    var preValue: Int?,
    var postValue: Int?,
    var date: Long?,
    @NonNull var counterId: Long) {

  companion object {
    const val TABLE_NAME = "changes"
    const val COUNTER_ID = "counterId"
  }

  @PrimaryKey
  @NonNull
  var id: Long? = null

}