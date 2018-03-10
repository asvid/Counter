package asvid.counter.repository

import asvid.counter.data.room.counter.CounterDao
import asvid.counter.model.Counter
import asvid.counter.repository.mappers.toEntity
import asvid.counter.repository.mappers.toModel
import asvid.counter.repository.reactive.RxCrudRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class CounterRepository @Inject constructor(
    var counterDao: CounterDao) :
    RxCrudRepository<Counter, Long> {

  override fun delete(model: Counter): Completable {
    return Completable.fromAction { counterDao.delete(model.toEntity()) }
  }

  override fun deleteAll(models: Collection<Counter>): Completable {
    return Completable.fromAction {
      models.forEach {
        delete(it)
      }
    }
  }

  override fun fetchAll(): Flowable<Collection<Counter>> {
    return counterDao.getAllCounters().map { it.toModel() }
  }

  override fun fetchById(id: Long): Maybe<Counter> {
    return counterDao.findCounterById(id).map { it.toModel() }
  }

  override fun save(model: Counter): Single<Counter> {
    return Single.fromCallable {
      val id = counterDao.save(model.toEntity())
      model.id = id
      return@fromCallable model
    }
  }

  override fun saveAll(models: Collection<Counter>): Single<Collection<Counter>> {
    return Single.fromCallable {
      models.map {
        val id = counterDao.save(it.toEntity())
        it.id = id
      }
      models
    }
  }

}