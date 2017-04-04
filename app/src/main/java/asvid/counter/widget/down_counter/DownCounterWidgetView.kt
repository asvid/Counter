package asvid.counter.widget.views

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.RemoteViews
import asvid.counter.R
import asvid.counter.R.id
import asvid.counter.data.down_counter.DownCounterWidget
import asvid.counter.dpToPx
import asvid.counter.widget.CounterWidgetProvider
import asvid.counter.widget.down_counter.DownCounterWidgetProvider
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.properties.Delegates

class DownCounterWidgetView(val context: Context) {

    var drawable: GradientDrawable by Delegates.notNull()

    fun setStrokeColor(color: Int) {
        drawable = ContextCompat.getDrawable(context,
            R.drawable.widget_background) as GradientDrawable
        drawable.setStroke(dpToPx(STROKE_SIZE_IN_ID), color)
    }

    fun getBitmap(widthFactor: Int): Bitmap {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            dpToPx(STD_SIZE * widthFactor + STD_PADDING_SIZE * (widthFactor - 1)), dpToPx(STD_SIZE),
            Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0,
            dpToPx(STD_SIZE * widthFactor + STD_PADDING_SIZE * (widthFactor - 1)), dpToPx(STD_SIZE))
        drawable.draw(canvas)

        return bitmap
    }

    fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: DownCounterWidget, remoteView: RemoteViews) {
        remoteView.setTextViewText(R.id.downCounterName, widget.name)
        remoteView.setTextViewText(R.id.downCounterValue, getValueFromDate(widget.date!!))
        setStrokeColor(widget.color!!)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap(2))

        remoteView.setOnClickPendingIntent(id.counterView,
            getPendingIntent(widgetId))

        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }

    fun getPendingIntent(widgetId: Int): PendingIntent {
        val intent = Intent(context, DownCounterWidgetProvider::class.java)

        intent.action = CounterWidgetProvider.UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

        return PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getValueFromDate(date: Date): String {
        val currentTime = Date()
        val diff = date.time - currentTime.time

        val dayUnit = context.resources.getString(R.string.dayUnit)
        val hourUnit = context.resources.getString(R.string.hourUnit)

        val daysDiff = TimeUnit.DAYS.convert(diff, MILLISECONDS)
        val hourDiff = TimeUnit.HOURS.convert(diff - (daysDiff * 24 * 60 * 60 * 1000), MILLISECONDS)

        Timber.d(
            "diff: $diff days: $daysDiff hourDiff: ${diff - (daysDiff * 24 * 60 * 60 * 1000)} | $hourDiff")

        return "$daysDiff $dayUnit $hourDiff $hourUnit"
    }

}