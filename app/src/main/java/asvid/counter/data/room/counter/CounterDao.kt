package asvid.counter.data.room.counter

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import asvid.counter.data.room.counter.CounterEntity.Companion.TABLE_NAME


@Dao
interface CounterDao {

  @Insert
  fun insert(counterEntity: CounterEntity)

  @Update
  fun update(vararg repos: CounterEntity)

  @Delete
  fun delete(vararg repos: CounterEntity)

  @Query("SELECT * FROM $TABLE_NAME")
  fun getAllCounters(): List<CounterEntity>

  @Query("SELECT * FROM $TABLE_NAME WHERE id=:counterId")
  fun findCounterById(counterId: Long): List<CounterEntity>

}