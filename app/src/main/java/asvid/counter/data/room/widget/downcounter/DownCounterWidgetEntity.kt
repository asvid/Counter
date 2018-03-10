package asvid.counter.data.room.widget.downcounter

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import asvid.counter.data.room.converters.Converters
import asvid.counter.data.room.widget.WidgetSize
import asvid.counter.data.room.widget.downcounter.DownCounterWidgetEntity.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME)
@TypeConverters(Converters::class)
class DownCounterWidgetEntity {

  companion object {
    const val TABLE_NAME = "DOWN_COUNTER_WIDGET"
    const val ID = "id"
  }

  @PrimaryKey
  @NonNull
  var id: Long? = null

  var color: Int? = null
  var selectedDate: Date? = null
  var name: String? = null

  @Embedded
  var size: WidgetSize? = null

}