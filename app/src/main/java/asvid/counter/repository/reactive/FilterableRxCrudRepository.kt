package asvid.counter.repository.reactive

import io.reactivex.Flowable

interface FilterableRxCrudRepository<M, ID, F> : RxCrudRepository<M, ID> {
  fun fetchBy(filter: F): Flowable<Collection<M>>
}
