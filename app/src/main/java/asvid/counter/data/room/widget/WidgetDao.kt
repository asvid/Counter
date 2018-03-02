package asvid.counter.data.room.widget

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import asvid.counter.data.room.counter.changes.ChangesEntity
import io.reactivex.Flowable

@Dao
interface WidgetDao {

  @Insert
  fun insert(counterEntity: WidgetEntity)

  @Update
  fun update(vararg repos: WidgetEntity)

  @Delete
  fun delete(vararg repos: WidgetEntity)

  @Query("SELECT * FROM ${WidgetEntity.TABLE_NAME}")
  fun getAllWidgets(): Flowable<List<ChangesEntity>>

  @Query("SELECT * FROM ${WidgetEntity.TABLE_NAME} WHERE id=:widgetId")
  fun findWidgetById(widgetId: Long): Flowable<List<WidgetEntity>>
}