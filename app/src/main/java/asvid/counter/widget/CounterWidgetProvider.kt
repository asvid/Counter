package asvid.counter.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.custom_views.WidgetView
import asvid.counter.data.CounterItemManager
import asvid.counter.data.Storage
import asvid.counter.analytics.enums.Action
import asvid.counter.analytics.enums.Category
import timber.log.Timber

/**
 * Created by adam on 14.01.17.
 */


class CounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray) {
        // Get all ids
        val thisWidget = ComponentName(context,
            CounterWidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            // create some random data
            val remoteViews = RemoteViews(context.packageName,
                R.layout.counter_appwidget)

            // Register an onClickListener
            setOnClick(context, widgetId.toLong(), remoteViews)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1).toLong()
        when (intent.action) {
            CLICKED -> widgetClicked(context, widgetId)
            UPDATE -> widgetUpdate(context, widgetId)
            APPWIDGET_DELETED -> widgetDeleted(context, widgetId)
            APPWIDGET_UPDATE -> updateAllWidgets(context)
        }
    }

    private fun updateAllWidgets(context: Context) {
        val storage = Storage(context)
        storage.getAllWidgets().map {
            CounterWidgetProvider.updateAppWidget(context, it.id!!, it)
        }
    }

    private fun widgetDeleted(context: Context, widgetId: Long) {
        Di.analyticsHelper.sendEvent(Category.WIDGET, Action.DELETE, "")
        val storage = Storage(context)
        val widget = storage.getWidget(widgetId.toInt())

        storage.deleteWidget(widget)
    }

    private fun widgetUpdate(context: Context, widgetId: Long) {
        val storage = Storage(context)
        if (widgetId > -1) {
            val widget = storage.getWidget(widgetId.toInt())
            val item = widget.counterItem!!
            Timber.d("updating asvid.counter.widget item: $item asvid.counter.widget: $widget")
            updateAppWidget(context, widgetId, widget)
        }
    }

    private fun widgetClicked(context: Context, widgetId: Long) {
        Di.analyticsHelper.sendEvent(Category.WIDGET, Action.CLICKED, "")
        val storage = Storage(context)
        Timber.d("widgetClicked: $widgetId")
        val widget = storage.getWidget(widgetId.toInt())

        val item = widget.counterItem
        if (item != null) {
            CounterItemManager.incrementAndSave(item)
        }
        updateAppWidget(context, widgetId, widget)
    }

    companion object {

        val UPDATE: String = "UPDATE"
        val CLICKED = "CLICKED"
        val APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED"
        val APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"

        private fun setOnClick(context: Context, widgetId: Long,
            remoteViews: RemoteViews) {
            val intent = Intent(context, CounterWidgetProvider::class.java)

            intent.action = CLICKED
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId.toInt())

            val pendingIntent = PendingIntent
                .getBroadcast(context, widgetId.toInt(), intent, 0)
            remoteViews
                .setOnClickPendingIntent(R.id.counterView, pendingIntent)
        }

        fun updateAppWidget(context: Context,
            mAppWidgetId: Long, item: CounterWidget) {
            val appWidgetManager = AppWidgetManager
                .getInstance(context)
            val views = RemoteViews(context.packageName,
                R.layout.counter_appwidget)

            val widgetView = WidgetView(context)
            if (item.counterItem != null) {
                widgetView.setNameText(item.counterItem?.name)
                widgetView.setValueText(item.counterItem?.value)
                widgetView.setStrokeColor(item.color)
                views.setImageViewBitmap(R.id.imageView, widgetView.getBitmap())

                setOnClick(context, mAppWidgetId, views)
            } else {
                widgetView.setNameText(context.resources.getString(R.string.counter_removed))
                widgetView.setValueText(0)
                widgetView.setStrokeColor(item.color)
                views.setImageViewBitmap(R.id.imageView, widgetView.getBitmap())
            }

            appWidgetManager.updateAppWidget(mAppWidgetId.toInt(), views)
        }
    }
}
