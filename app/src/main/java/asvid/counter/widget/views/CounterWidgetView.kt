package asvid.counter.widget.views

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.RemoteViews
import asvid.counter.R
import asvid.counter.R.string
import asvid.counter.dpToPx
import asvid.counter.getLastItem
import asvid.counter.data.widget.CounterWidget
import asvid.counter.widget.CounterWidgetProvider
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber
import kotlin.properties.Delegates

val STROKE_SIZE_IN_ID = 5
val INCREMENT_CLICKED = "INCREMENT_CLICKED"
val DECREMENT_CLICKED = "DECREMENT_CLICKED"
val SINGLE_ACTION = "SINGLE_ACTION"
val BUTTON_ACTION = "BUTTON_ACTION"
val STD_SIZE = 68

class CounterWidgetView(val context: Context) {

    var drawable: GradientDrawable by Delegates.notNull()

    fun setStrokeColor(color: Int) {
        drawable = ContextCompat.getDrawable(context,
            R.drawable.widget_background) as GradientDrawable
        drawable.setStroke(dpToPx(STROKE_SIZE_IN_ID), color)
    }

    fun getBitmap(): Bitmap {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(dpToPx(STD_SIZE * 3), dpToPx(STD_SIZE),
            Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, dpToPx(STD_SIZE * 3), dpToPx(STD_SIZE))
        drawable.draw(canvas)

        return bitmap
    }

    fun getPendingIntent(widgetId: Int, action: String): PendingIntent {
        Timber.d("getPendingIntent action: $action")
        val intent = Intent(context, CounterWidgetProvider::class.java)

        intent.action = CounterWidgetProvider.CLICKED
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        intent.putExtra(BUTTON_ACTION, action)

        return PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), intent,
            FLAG_ONE_SHOT)
    }

    fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        remoteView.setTextViewText(R.id.name, widget.counterItem?.name)
        remoteView.setTextViewText(R.id.value, widget.counterItem?.value.toString())
        if (widget.counterItem!!.changes.isNotEmpty()) remoteView.setTextViewText(R.id.lastChange,
            getLastChangePretyTime(widget))
        setStrokeColor(widget.color!!)
        Timber.d("bitmap: ${getBitmap().byteCount}")
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())

        remoteView.setOnClickPendingIntent(R.id.widgetAddButton,
            getPendingIntent(widgetId, INCREMENT_CLICKED))

        remoteView.setOnClickPendingIntent(R.id.widgetMinusButton,
            getPendingIntent(widgetId, DECREMENT_CLICKED))

        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }

    private fun getLastChangePretyTime(widget: CounterWidget) = PrettyTime().format(
        widget.counterItem!!.changes.getLastItem().date)

    fun setInactive(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        remoteView.setTextViewText(R.id.name, context.resources.getString(string.counter_removed))
        remoteView.setTextViewText(R.id.value, "X")
        setStrokeColor(widget.color!!)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())
        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }
}