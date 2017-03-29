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

class WidgetView1x2(context: Context) : BaseWidgetView(context) {

    override val WIDTH_IN_DP = 136
    override val HEIGHT_IN_DP = 68

    init {
        inflate(getContext(), layout.counter_widget_layout_1x2, this)
        this.nameView = findViewById(R.id.name) as TextView
        this.valueView = findViewById(R.id.value) as TextView
        this.counterView = findViewById(R.id.counterView) as FrameLayout
    }

    private fun showButtons(remoteView: RemoteViews) {
        Timber.d("showing buttons")
        remoteView.setViewVisibility(R.id.widgetAddButton, View.VISIBLE)
        remoteView.setViewVisibility(R.id.widgetMinusButton, View.VISIBLE)
    }

    override fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        setNameText(widget.counterItem?.name)
        setValueText(widget.counterItem?.value)
        setStrokeColor(widget.color)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())

        showButtons(remoteView)
        remoteView.setOnClickPendingIntent(R.id.widgetAddButton,
            getPendingIntent(widgetId, INCREMENT_CLICKED))

        remoteView.setOnClickPendingIntent(R.id.widgetMinusButton,
            getPendingIntent(widgetId, DECREMENT_CLICKED))

        appWidgetManager.updateAppWidget(widgetId, remoteView)
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