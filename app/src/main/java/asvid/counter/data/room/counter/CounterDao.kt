package asvid.counter.data.room.counter

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import asvid.counter.data.room.counter.CounterEntity.Companion.TABLE_NAME
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface CounterDao {

  @Insert(onConflict = REPLACE)
  fun save(counterEntity: CounterEntity): Long

  @Delete
  fun delete(vararg repos: CounterEntity)

  @Query("SELECT * FROM $TABLE_NAME")
  fun getAllCounters(): Flowable<List<CounterEntity>>

  @Query("SELECT * FROM $TABLE_NAME WHERE id=:counterId")
  fun findCounterById(counterId: Long): Maybe<CounterEntity>

}