package asvid.counter.data.room.changes

import javax.inject.Inject

class ChangesRepository {

  var changesDao: ChangesDao? = null
    @Inject set

}