package widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import asvid.beercounter.R
import asvid.beercounter.data.CounterItem
import asvid.beercounter.data.CounterItemManager
import asvid.beercounter.data.Storage
import timber.log.Timber

/**
 * Created by adam on 14.01.17.
 */


class BeerCounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray) {
        // Get all ids
        val thisWidget = ComponentName(context,
            BeerCounterWidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            // create some random data
            val remoteViews = RemoteViews(context.packageName,
                R.layout.beer_counter_appwidget)

            // Register an onClickListener
            setOnClick(context, widgetId, remoteViews)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.d("BeerCounterWidgetProvider onReceive")
        when (intent.action) {
            CLICKED -> widgetClicked(context, intent)
            UPDATE -> widgetUpdate(context, intent)
        }
    }

    private fun widgetUpdate(context: Context, intent: Intent) {
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val storage = Storage(context)
        val widget = storage.getWidget(widgetId)
        val item = widget.counterItem!!
        Timber.d("updating widget item: $item widget: $widget")
        updateAppWidget(context, widgetId, item)
    }

    private fun widgetClicked(context: Context, intent: Intent) {
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)


        val storage = Storage(context)
        val widget = storage.getWidget(widgetId)

        val item = widget.counterItem!!
        CounterItemManager.incrementAndSave(item)

        updateAppWidget(context, widgetId, item)
    }

    companion object {

        val UPDATE: String = "UPDATE"
        val CLICKED = "CLICKED"

        private fun setOnClick(context: Context, widgetId: Int,
            remoteViews: RemoteViews) {
            val intent = Intent(context, BeerCounterWidgetProvider::class.java)

            intent.action = CLICKED
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

            val pendingIntent = PendingIntent
                .getBroadcast(context, widgetId, intent, 0)
            remoteViews
                .setOnClickPendingIntent(R.id.beerCounterView, pendingIntent)
        }

        fun updateAppWidget(context: Context,
            mAppWidgetId: Int, item: CounterItem) {
            val appWidgetManager = AppWidgetManager
                .getInstance(context)
            val views = RemoteViews(context.packageName,
                R.layout.beer_counter_appwidget)
            views.setTextViewText(R.id.name, item.name.toString())
            views.setTextViewText(R.id.value, item.value.toString())
            setOnClick(context, mAppWidgetId, views)
            appWidgetManager.updateAppWidget(mAppWidgetId, views)
        }
    }
}
