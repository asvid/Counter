package asvid.counter.data.room.widget

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import asvid.counter.data.room.converters.Converters
import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.data.room.widget.WidgetEntity.Companion.TABLE_NAME
import io.realm.annotations.PrimaryKey
import java.util.Date

@Entity(tableName = TABLE_NAME,
    foreignKeys = (arrayOf(ForeignKey(
        entity = CounterEntity::class,
        parentColumns = [(WidgetEntity.ID)],
        childColumns = [(CounterEntity.ID)],
        onDelete = ForeignKey.NO_ACTION))))
@TypeConverters(Converters::class)
class WidgetEntity(@PrimaryKey @NonNull var id: Long) {

  companion object {
    const val TABLE_NAME = "widgets"
    const val ID = "id"
  }

  var color: Int? = null
  var createDate: Date? = null

  @Embedded
  var size: WidgetSize? = null

}