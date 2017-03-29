package asvid.counter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.RemoteViews
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.analytics.enums.Action
import asvid.counter.analytics.enums.Category
import asvid.counter.data.CounterItemManager
import asvid.counter.data.Storage
import asvid.counter.widget.views.BUTTON_ACTION
import asvid.counter.widget.views.BaseWidgetView
import asvid.counter.widget.views.DECREMENT_CLICKED
import asvid.counter.widget.views.INCREMENT_CLICKED
import asvid.counter.widget.views.WidgetView1x1
import asvid.counter.widget.views.WidgetView1x2
import timber.log.Timber

/**
 * Created by adam on 14.01.17.
 */


class CounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray) {
        val thisWidget = ComponentName(context,
            CounterWidgetProvider::class.java)
        val storage = Storage(context)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            val widget = storage.getWidget(widgetId)
            updateAppWidget(context, widgetId.toLong(), widget)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1).toLong()
        val buttonAction = intent.getStringExtra(BUTTON_ACTION)
        Timber.d("onReceive $widgetId $buttonAction ${intent.action}")
        when (intent.action) {
            CLICKED -> widgetClicked(context, widgetId, buttonAction)
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
            updateAppWidget(context, widgetId, widget)
        }
    }

    //  TODO move to other service
    private fun widgetClicked(context: Context, widgetId: Long,
        buttonAction: String) {
        Di.analyticsHelper.sendEvent(Category.WIDGET, Action.CLICKED, "")
        val storage = Storage(context)
        Timber.d("widgetClicked: $buttonAction")
        val widget = storage.getWidget(widgetId.toInt())

        val item = widget.counterItem
        if (item != null && !TextUtils.isEmpty(buttonAction)) {
            when (buttonAction) {
                INCREMENT_CLICKED -> CounterItemManager.incrementAndSave(item)
                DECREMENT_CLICKED -> CounterItemManager.decrementAndSave(item)
            }
        }

        updateAppWidget(context, widgetId, widget)
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int, newOptions: Bundle?) {
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        Timber.d("${getCellsForSize(minHeight)} ${getCellsForSize(minWidth)} ")

        val newSize = getNewSize(getCellsForSize(minHeight), getCellsForSize(minWidth))

        val storage = Storage(context)
        val widget = storage.getWidget(appWidgetId)
        widget.size = newSize
        storage.saveWidget(widget)
        Timber.d("new size: $newSize")
        updateAppWidget(context, appWidgetId.toLong(), widget)
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

        fun updateAppWidget(context: Context,
            widgetId: Long, item: CounterWidget) {
            val appWidgetManager = AppWidgetManager
                .getInstance(context)
            val remoteView = RemoteViews(context.packageName,
                R.layout.counter_appwidget)

            val widgetView = getWidgetView(context, item.size)
            if (item.counterItem != null) {
                widgetView.update(appWidgetManager, widgetId.toInt(), item, remoteView)
            } else {
                widgetView.setInactive(appWidgetManager, widgetId.toInt(), item, remoteView)
            }
        }

        private fun getWidgetView(context: Context, size: String?): BaseWidgetView {
            when (size) {
                "1x1" -> return WidgetView1x1(context)
                "1x2" -> return WidgetView1x2(context)
            }
            return WidgetView1x1(context)
        }
    }
}
