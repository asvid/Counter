package asvid.counter.repository

import asvid.counter.data.room.widget.downcounter.DownCounterWidgetDao
import asvid.counter.data.room.widget.downcounter.DownCounterWidgetEntity
import javax.inject.Inject

class DownCounterWidgetRepository @Inject constructor(
    var downCounterWidgetDao: DownCounterWidgetDao) {

  fun getAll() = downCounterWidgetDao.getAllWidgets()
  fun getWidget(widgetId: Long) = downCounterWidgetDao.findWidgetById(widgetId)
  fun updateWidget(widget: DownCounterWidgetEntity) = downCounterWidgetDao.update(widget)
  fun createWidget(widget: DownCounterWidgetEntity) = downCounterWidgetDao.insert(widget)
  fun deleteWidget(widget: DownCounterWidgetEntity) = downCounterWidgetDao.delete(widget)
}