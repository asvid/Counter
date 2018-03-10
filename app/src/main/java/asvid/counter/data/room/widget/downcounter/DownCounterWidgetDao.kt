package asvid.counter.data.room.widget.downcounter

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface DownCounterWidgetDao {

  @Insert
  fun insert(downCounterWidgetEntity: DownCounterWidgetEntity)

  @Update
  fun update(vararg repos: DownCounterWidgetEntity)

  @Delete
  fun delete(vararg repos: DownCounterWidgetEntity)

  @Query("SELECT * FROM ${DownCounterWidgetEntity.TABLE_NAME}")
  fun getAllWidgets(): Flowable<List<DownCounterWidgetEntity>>

  @Query("SELECT * FROM ${DownCounterWidgetEntity.TABLE_NAME} WHERE id=:widgetId")
  fun findWidgetById(widgetId: Long): Flowable<List<DownCounterWidgetEntity>>
}