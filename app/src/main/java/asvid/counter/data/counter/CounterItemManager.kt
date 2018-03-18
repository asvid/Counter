package asvid.counter.data.counter

import android.appwidget.AppWidgetManager
import android.content.Intent
import asvid.counter.di.Di.context
import asvid.counter.model.Counter
import asvid.counter.repository.CounterRepository
import asvid.counter.widget.CounterWidgetProvider
import io.reactivex.Flowable
import io.reactivex.Maybe
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
  }

  fun getAllCounterItems(): Flowable<Collection<Counter>> {
    return counterRepository.fetchAll()
  }

  fun getCounterItem(id: Long): Maybe<Counter> {
    return counterRepository.fetchById(id)
  }

  fun saveCounterItem(counter: Counter) {
    counterRepository.save(counter)
  }

  fun createNewCounter(name: String, value: Int) {
    val counterEntity = Counter(null, name, value)
    counterRepository.save(counterEntity)
  }

  fun deleteCounterItem(counter: Counter) {
    counterRepository.delete(counter)
  }

}