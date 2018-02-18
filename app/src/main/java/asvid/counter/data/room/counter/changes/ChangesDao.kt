package asvid.counter.data.room.counter.changes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import asvid.counter.data.room.counter.changes.ChangesEntity.Companion.TABLE_NAME

@Dao
interface ChangesDao {

  @Insert
  fun insert(counterEntity: ChangesEntity)

  @Update
  fun update(vararg repos: ChangesEntity)

  @Delete
  fun delete(vararg repos: ChangesEntity)

  @Query("SELECT * FROM $TABLE_NAME")
  fun getAllRepos(): List<ChangesEntity>

  @Query("SELECT * FROM $TABLE_NAME WHERE counterId=:counterId")
  fun findChangesForCounter(counterId: Long): List<ChangesEntity>

}