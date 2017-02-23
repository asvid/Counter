package widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

import asvid.beercounter.R
import asvid.beercounter.data.CounterItem
import asvid.beercounter.data.Storage

/**
 * Created by adam on 14.01.17.
 */

class BeerCounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray) {

        Log.d("WIDGET", "onUpdate ")
        // Get all ids
        val thisWidget = ComponentName(context,
            BeerCounterWidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            // create some random data

            Log.d("WIDGET", "widgetId " + widgetId)
            val remoteViews = RemoteViews(context.packageName,
                R.layout.beer_counter_appwidget)

            // Register an onClickListener
            setOnClick(context, widgetId, remoteViews)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == CLICKED) {
            val widgetId = intent
                .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            val appWidgetManager = AppWidgetManager
                .getInstance(context)

            val storage = Storage(context)
            val widget = storage.getWidget(widgetId)

            val item = widget.counterItem!!
            storage.increaseAndSave(item)

            updateAppWidget(context, appWidgetManager, widgetId, item)

            Log.d("WIDGET", "onReceive " + widgetId)
        }
    }

    companion object {

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
            appWidgetManager: AppWidgetManager,
            mAppWidgetId: Int, item: CounterItem) {
            val views = RemoteViews(context.packageName,
                R.layout.beer_counter_appwidget)
            views.setTextViewText(R.id.name, item.name.toString())
            views.setTextViewText(R.id.value, item.value.toString())
            setOnClick(context, mAppWidgetId, views)
            appWidgetManager.updateAppWidget(mAppWidgetId, views)
        }
    }
}
