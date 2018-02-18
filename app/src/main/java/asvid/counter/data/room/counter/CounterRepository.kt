package asvid.counter.data.room.counter

import javax.inject.Inject

class CounterRepository @Inject constructor(var counterDao: CounterDao) {

  fun getAll() = counterDao.getAllCounters()
  fun getCounter(counterId: Long) = counterDao.findCounterById(counterId)
  fun updateCounter(counter: CounterEntity) = counterDao.update(counter)
  fun createNewCounter(counter: CounterEntity) = counterDao.insert(counter)
  fun deleteCounter(counter: CounterEntity) = counterDao.delete(counter)

}