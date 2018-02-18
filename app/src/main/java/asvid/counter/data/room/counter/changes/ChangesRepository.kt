package asvid.counter.data.room.counter.changes

import javax.inject.Inject

class ChangesRepository {

  lateinit var changesDao: ChangesDao
    @Inject set

  fun getCounterChanges(counterId: Long) = changesDao.findChangesForCounter(counterId)
}