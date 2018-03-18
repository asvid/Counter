package asvid.counter.data.room.counter.changes

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import asvid.counter.data.room.converters.Converters
import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.data.room.counter.changes.ChangesEntity.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME,
    foreignKeys = (arrayOf(ForeignKey(
        entity = CounterEntity::class,
        parentColumns = [(CounterEntity.ID)],
        childColumns = [(ChangesEntity.ID)],
        onDelete = CASCADE))))
@TypeConverters(Converters::class)
data class ChangesEntity(
    var preValue: Int?,
    var postValue: Int?,
    var date: Date?,
    @NonNull var counterId: Long) {

  companion object {
    const val TABLE_NAME = "changes"
    const val ID = "counterId"
  }

  @PrimaryKey(autoGenerate = true)
  @NonNull
  var id: Long? = null

}