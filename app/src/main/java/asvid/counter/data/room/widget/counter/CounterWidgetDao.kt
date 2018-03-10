package asvid.counter.data.room.widget.counter

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface CounterWidgetDao {

  @Insert
  fun insert(counterEntityCounter: CounterWidgetEntity)

  @Update
  fun update(vararg repos: CounterWidgetEntity)

  @Delete
  fun delete(vararg repos: CounterWidgetEntity)

  @Query("SELECT * FROM ${CounterWidgetEntity.TABLE_NAME}")
  fun getAllWidgets(): Flowable<List<CounterWidgetEntity>>

  @Query("SELECT * FROM ${CounterWidgetEntity.TABLE_NAME} WHERE id=:widgetId")
  fun findWidgetById(widgetId: Long): Flowable<List<CounterWidgetEntity>>
}