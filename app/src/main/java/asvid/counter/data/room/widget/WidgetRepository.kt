package asvid.counter.data.room.widget

import javax.inject.Inject

class WidgetRepository @Inject constructor(var widgetDao: WidgetDao) {

  fun getAll() = widgetDao.getAllWidgets()
  fun getWidget(widgetId: Long) = widgetDao.findWidgetById(widgetId)
  fun updateWidget(widget: WidgetEntity) = widgetDao.update(widget)
  fun createWidget(widget: WidgetEntity) = widgetDao.insert(widget)
  fun deleteWidget(widget: WidgetEntity) = widgetDao.delete(widget)

}