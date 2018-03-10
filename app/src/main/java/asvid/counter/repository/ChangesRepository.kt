package asvid.counter.repository

import asvid.counter.data.room.counter.changes.ChangesDao
import javax.inject.Inject

class ChangesRepository @Inject constructor(private var changesDao: ChangesDao) {

  fun getCounterChanges(counterId: Long) = changesDao.findChangesForCounter(counterId)
}