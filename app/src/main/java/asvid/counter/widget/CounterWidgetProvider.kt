package asvid.counter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import asvid.counter.R
import asvid.counter.analytics.enums.Action
import asvid.counter.analytics.enums.Category
import asvid.counter.di.Di
import asvid.counter.model.CounterWidget
import asvid.counter.model.WidgetSize
import asvid.counter.widget.views.BUTTON_ACTION
import asvid.counter.widget.views.CounterWidgetView
import timber.log.Timber

class CounterWidgetProvider : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
      appWidgetIds: IntArray) {
    Timber.d("onUpdate $appWidgetIds")
    for (widgetId in appWidgetIds) {
      Timber.d("widgetId $widgetId")
//      val widget = storage.getWidget(widgetId)
//      updateAppWidget(context, widgetId.toLong(), widget)
    }
  }

  override fun onReceive(context: Context, intent: Intent) {
    Timber.d("onReceive $intent")
    val widgetId = intent
        .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1).toLong()
    val buttonAction = intent.getStringExtra(BUTTON_ACTION)
    if (widgetId > -1)
      when (intent.action) {
        CLICKED -> widgetClicked(context, widgetId, buttonAction)
        UPDATE -> widgetUpdate(context, widgetId)
        APPWIDGET_DELETED -> widgetDeleted(context, widgetId)
      }
    else updateAllWidgets(context)
    super.onReceive(context, intent)
  }

  private fun updateAllWidgets(context: Context) {
//    val storage = Storage(context)
//    storage.getAllWidgets().map {
//      CounterWidgetProvider.updateAppWidget(context, it.id!!, it)
//    }
  }

  private fun widgetDeleted(context: Context, widgetId: Long) {
    Di.analyticsHelper.sendEvent(Category.WIDGET, Action.DELETE, "")
//    val widget = storage.getWidget(widgetId.toInt())
//
//    storage.deleteWidget(widget)
  }

  private fun widgetUpdate(context: Context, widgetId: Long) {
    if (widgetId > -1) {
//      val widget = storage.getWidget(widgetId.toInt())
//      updateAppWidget(context, widgetId, widget)
    }
  }

  //  TODO move to other service
  private fun widgetClicked(context: Context, widgetId: Long,
      buttonAction: String) {
    Di.analyticsHelper.sendEvent(Category.WIDGET, Action.CLICKED, "")
    Timber.d("widgetClicked: $buttonAction")
//    val widget = storage.getWidget(widgetId.toInt())

//    val item = widget.counterItem
//    if (item != null && !TextUtils.isEmpty(buttonAction)) {
//      when (buttonAction) {
//        INCREMENT_CLICKED -> CounterItemManager.incrementAndSave(item)
//        DECREMENT_CLICKED -> CounterItemManager.decrementAndSave(item)
//        SINGLE_ACTION -> CounterItemManager.incrementAndSave(item)
//      }
//    }

//    updateAppWidget(context, widgetId, widget)
  }

  override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager,
      appWidgetId: Int, newOptions: Bundle?) {
    val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

    val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

    Timber.d("${getCellsForSize(minHeight)} ${getCellsForSize(minWidth)} ")

    val newSize = getNewSize(getCellsForSize(minHeight), getCellsForSize(minWidth))

//    val widget = storage.getWidget(appWidgetId)
//    val size = WidgetSize()
//    size.heightFactor = getCellsForSize(minHeight)
//    size.widthFactor = getCellsForSize(minWidth)
//    widget.size = size
//    storage.saveWidget(widget)
//    Timber.d("new size: $newSize")
//    updateAppWidget(context, appWidgetId.toLong(), widget)
    super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

  }

  private fun getNewSize(height: Int, width: Int): String {
    return "${height}x$width"
  }

  fun getCellsForSize(size: Int): Int {
    var n = 2
    while (70 * n - 30 < size) {
      ++n
    }
    return n - 1
  }

  companion object {

    val UPDATE: String = "UPDATE"
    val CLICKED = "CLICKED"
    val APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED"
    val APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"

    fun getRemoteViews(context: Context, size: WidgetSize): RemoteViews {
      when (size.widthFactor) {
        1 -> return RemoteViews(context.packageName,
            R.layout.counter_widget_layout_1x1)
        2 -> return RemoteViews(context.packageName,
            R.layout.counter_widget_layout_1x2)
      }
      if (size.widthFactor >= 3) return RemoteViews(context.packageName,
          R.layout.counter_widget_layout_1x3)
      return RemoteViews(context.packageName, R.layout.counter_widget_layout_1x1)
    }

    fun updateAppWidget(context: Context,
        widgetId: Long, item: CounterWidget) {
      val remoteView = getRemoteViews(context, item.size!!)
      val appWidgetManager = AppWidgetManager
          .getInstance(context)

      val widgetView = CounterWidgetView(context)
      if (item.counter != null) {
        widgetView.update(appWidgetManager, widgetId.toInt(), item, remoteView)
      } else {
        widgetView.setInactive(appWidgetManager, widgetId.toInt(), item, remoteView)
      }
    }
  }
}
