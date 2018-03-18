package asvid.counter.data.room.widget.counter

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import asvid.counter.data.room.converters.Converters
import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.data.room.widget.WidgetSize
import asvid.counter.data.room.widget.counter.CounterWidgetEntity.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME,
    foreignKeys = (arrayOf(ForeignKey(
        entity = CounterEntity::class,
        parentColumns = [(CounterEntity.ID)],
        childColumns = ["counterId"],
        onDelete = ForeignKey.NO_ACTION))))
@TypeConverters(Converters::class)
class CounterWidgetEntity {

  companion object {
    const val TABLE_NAME = "COUNTER_WIDGET"
    const val ID = "id"
  }

  @PrimaryKey(autoGenerate = true)
  @NonNull
  var id: Long? = null

  var counterId: Long? = null
  var color: Int? = null
  var createDate: Date? = null

  @Embedded
  var size: WidgetSize? = null

}