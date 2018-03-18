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
import asvid.counter.R.id
import asvid.counter.R.string
import asvid.counter.dpToPx
import asvid.counter.getLastItem
import asvid.counter.model.CounterWidget
import asvid.counter.widget.CounterWidgetProvider
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber
import kotlin.properties.Delegates

val STROKE_SIZE_IN_ID = 5
val INCREMENT_CLICKED = "INCREMENT_CLICKED"
val DECREMENT_CLICKED = "DECREMENT_CLICKED"
val SINGLE_ACTION = "SINGLE_ACTION"
val BUTTON_ACTION = "BUTTON_ACTION"
val STD_SIZE = 72
val STD_PADDING_SIZE = 10

class CounterWidgetView(val context: Context) {

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
//        remoteView.setTextViewText(R.id.counterName, widget.counterItem?.name)
//        remoteView.setTextViewText(R.id.counterStartValue, widget.counterItem?.value.toString())
//        if (widget.counterItem!!.changes.isNotEmpty()) remoteView.setTextViewText(R.id.lastChange,
//            getLastChangePretyTime(widget))
//        setStrokeColor(widget.color!!)
//        remoteView.setImageViewBitmap(R.id.imageView, getBitmap(widget.size!!.widthFactor!!))
//
//        setActions(remoteView, widget, widgetId)
//
//        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }

    private fun setActions(remoteView: RemoteViews,
        widget: CounterWidget, widgetId: Int) {
        if (widget.size!!.widthFactor == 1) {
            remoteView.setOnClickPendingIntent(id.counterView,
                getPendingIntent(widgetId, SINGLE_ACTION))
        } else {
            remoteView.setOnClickPendingIntent(id.widgetAddButton,
                getPendingIntent(widgetId, INCREMENT_CLICKED))

            remoteView.setOnClickPendingIntent(id.widgetMinusButton,
                getPendingIntent(widgetId, DECREMENT_CLICKED))
        }
    }

    private fun getLastChangePretyTime(widget: CounterWidget) = PrettyTime().format(
        widget.counter.changes.getLastItem().date)

    fun setInactive(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        remoteView.setTextViewText(R.id.counterName, context.resources.getString(string.counter_removed))
        remoteView.setTextViewText(R.id.counterStartValue, "X")
        setStrokeColor(widget.color!!)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap(widget.size!!.widthFactor!!))
        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }
}