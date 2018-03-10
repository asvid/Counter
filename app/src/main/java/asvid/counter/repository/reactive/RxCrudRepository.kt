package asvid.counter.repository.reactive

import asvid.counter.repository.Repository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RxCrudRepository<M, I> : Repository<M, I> {
  fun delete(model: M): Completable

  fun deleteAll(models: Collection<M>): Completable

  fun fetchAll(): Flowable<Collection<M>>

  fun fetchById(id: I): Maybe<M>

  fun save(model: M): Single<M>

  fun saveAll(models: Collection<M>): Single<Collection<M>>
}
