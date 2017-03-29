package asvid.counter.widget.views

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import asvid.counter.R
import asvid.counter.R.layout
import asvid.counter.R.string
import asvid.counter.widget.CounterWidget
import timber.log.Timber

class WidgetView1x1(context: Context) : BaseWidgetView(context) {

    init {
        inflate(getContext(), layout.counter_widget_layout_1x1, this)
        this.nameView = findViewById(R.id.name) as TextView
        this.valueView = findViewById(R.id.value) as TextView
        this.counterView = findViewById(R.id.counterView) as FrameLayout
    }

    override fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        setNameText(widget.counterItem?.name)
        setValueText(widget.counterItem?.value)
        setStrokeColor(widget.color)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())

        appWidgetManager.updateAppWidget(widgetId, remoteView)
        hideButtons(remoteView)
    }

    private fun hideButtons(remoteView: RemoteViews) {
        Timber.d("hiding buttons")
        remoteView.setViewVisibility(R.id.incrementButton, View.INVISIBLE)
        remoteView.setViewVisibility(R.id.decrementButton, View.INVISIBLE)
    }

    override fun setInactive(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        setNameText(context.resources.getString(string.counter_removed))
        setValueText("X")
        setStrokeColor(widget.color)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())
        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }
}