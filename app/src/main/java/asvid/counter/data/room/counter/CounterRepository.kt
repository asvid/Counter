package asvid.counter.data.room.counter

import javax.inject.Inject

class CounterRepository {

  var counterDao: CounterDao? = null
    @Inject set

}