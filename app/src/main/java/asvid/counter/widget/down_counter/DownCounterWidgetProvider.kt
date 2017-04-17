package asvid.counter.widget.down_counter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import asvid.counter.R.layout
import asvid.counter.data.Storage
import asvid.counter.data.down_counter.DownCounterWidget
import asvid.counter.widget.CounterWidgetProvider
import asvid.counter.widget.views.DownCounterWidgetView
import timber.log.Timber

class DownCounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray) {
        val thisWidget = ComponentName(context,
            DownCounterWidgetProvider::class.java)
        val storage = Storage(context)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            val widget = storage.getDownCounterWidget(widgetId)
            updateAppWidget(context, widgetId.toLong(), widget)
            Timber.d("DownCounter on update $widget")
        }

        Timber.d("DownCounter on update")
    }

    override fun onReceive(context: Context, intent: Intent) {
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1).toLong()
        Timber.d("onReceive $widgetId ")
        if (widgetId > -1) {
            val storage = Storage(context)
            val widget = storage.getDownCounterWidget(widgetId.toInt())
            when (intent.action) {
                CounterWidgetProvider.UPDATE -> updateAppWidget(context, widgetId, widget)
            }
        } else {
            updateAllWidgets(context)
        }
    }

    private fun updateAllWidgets(context: Context) {
        val storage = Storage(context)
        storage.getAllDownCounterWidget().map {
            DownCounterWidgetProvider.updateAppWidget(context, it.id!!, it)
        }
    }

    companion object {

        fun updateAppWidget(context: Context,
            widgetId: Long, item: DownCounterWidget) {
            val remoteView = RemoteViews(context.packageName,
                layout.down_counter_appwidget)
            val appWidgetManager = AppWidgetManager
                .getInstance(context)

            val widgetView = DownCounterWidgetView(context)
            widgetView.update(appWidgetManager, widgetId.toInt(), item, remoteView)
        }
    }
}
