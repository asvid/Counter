package asvid.counter.custom_views

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import asvid.counter.R
import asvid.counter.widget.CounterWidget

class WidgetView1x2(context: Context) : BaseWidgetView(context) {

    override val WIDTH_IN_DP = 136
    override val HEIGHT_IN_DP = 68

    init {
        inflate(getContext(), R.layout.counter_widget_layout_1x2, this)
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
    }

    override fun setInactive(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        setNameText(context.resources.getString(R.string.counter_removed))
        setValueText("X")
        setStrokeColor(widget.color)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())
        appWidgetManager.updateAppWidget(widgetId, remoteView)
    }

}