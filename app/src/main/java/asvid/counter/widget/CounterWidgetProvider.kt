package asvid.counter.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.RemoteViews
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.custom_views.WidgetBackground
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import asvid.counter.data.Storage
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
            setOnClick(context, widgetId, remoteViews)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.d("CounterWidgetProvider onReceive")
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
        Timber.d("updating asvid.counter.widget item: $item asvid.counter.widget: $widget")
        updateAppWidget(context, widgetId, widget)
    }

    private fun widgetClicked(context: Context, intent: Intent) {
        val widgetId = intent
            .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)


        val storage = Storage(context)
        val widget = storage.getWidget(widgetId)

        val item = widget.counterItem!!
        CounterItemManager.incrementAndSave(item)

        updateAppWidget(context, widgetId, widget)
    }

    companion object {

        val UPDATE: String = "UPDATE"
        val CLICKED = "CLICKED"

        private fun setOnClick(context: Context, widgetId: Int,
            remoteViews: RemoteViews) {
            val intent = Intent(context, CounterWidgetProvider::class.java)

            intent.action = CLICKED
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

            val pendingIntent = PendingIntent
                .getBroadcast(context, widgetId, intent, 0)
            remoteViews
                .setOnClickPendingIntent(R.id.counterView, pendingIntent)
        }

        fun updateAppWidget(context: Context,
            mAppWidgetId: Int, item: CounterWidget) {
            val appWidgetManager = AppWidgetManager
                .getInstance(context)
            val views = RemoteViews(context.packageName,
                R.layout.counter_appwidget)


            val myView = WidgetBackground(context)
            val size = Di.utils.dpToPx(68)
            myView.measure(size, size)
            myView.layout(0, 0, size, size)
            myView.setNameText(item.counterItem?.name)
            myView.setValueText(item.counterItem?.value)
            myView.setStrokeColor(item.color)

            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            myView.draw(Canvas(bitmap))
            views.setImageViewBitmap(R.id.imageView, bitmap)

            setOnClick(context, mAppWidgetId, views)
            appWidgetManager.updateAppWidget(mAppWidgetId, views)
        }
    }
}
