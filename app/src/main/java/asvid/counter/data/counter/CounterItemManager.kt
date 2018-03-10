package asvid.counter.data.counter

import android.appwidget.AppWidgetManager
import android.content.Intent
import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.repository.CounterRepository
import asvid.counter.di.Di
import asvid.counter.di.Di.context
import asvid.counter.model.Counter
import asvid.counter.widget.CounterWidgetProvider
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

object CounterItemManager {

  lateinit var counterRepository: CounterRepository
    @Inject set

  fun updateWidget(id: Long?) {
    val intent = Intent(context, CounterWidgetProvider::class.java)
    intent.action = CounterWidgetProvider.UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id?.toInt())

    context.sendBroadcast(intent)
  }

  fun saveAndUpdateWidget(counter: Counter) {
    counterRepository.save(counter)
    Di.storage.getWidgetsOfCounter(counterEntity).map { updateWidget(it.id) }
  }

  fun getAllCounterItems(): Flowable<List<CounterEntity>> {
    return counterRepository.getAll()
  }

  fun getCounterItem(id: Long): CounterEntity {
    return counterRepository.getCounter(id)
  }

  fun saveCounterItem(counterEntity: CounterEntity) {
    counterRepository.updateCounter(counterEntity)
  }

  fun createNewCounter(name: String, value: Int) {
    val counterEntity = CounterEntity(name, value)
    counterRepository.createNewCounter(counterEntity)
  }

  fun deleteCounterItem(counterEntity: CounterEntity) {
    Di.storage.getWidgetsOfCounter(counterEntity).map { updateWidget(it.id) }
    counterRepository.deleteCounter(counterEntity)
  }

}