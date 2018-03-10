package asvid.counter.repository

import asvid.counter.data.room.widget.counter.CounterWidgetDao
import asvid.counter.data.room.widget.counter.CounterWidgetEntity
import javax.inject.Inject

class CounterWidgetRepository @Inject constructor(var counterWidgetDao: CounterWidgetDao) {

  fun getAll() = counterWidgetDao.getAllWidgets()
  fun getWidget(widgetId: Long) = counterWidgetDao.findWidgetById(widgetId)
  fun updateWidget(counterWidget: CounterWidgetEntity) = counterWidgetDao.update(counterWidget)
  fun createWidget(counterWidget: CounterWidgetEntity) = counterWidgetDao.insert(counterWidget)
  fun deleteWidget(counterWidget: CounterWidgetEntity) = counterWidgetDao.delete(counterWidget)

}