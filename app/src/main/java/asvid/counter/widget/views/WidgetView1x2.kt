package asvid.counter.widget.views

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import asvid.counter.R
import asvid.counter.R.layout
import asvid.counter.R.string
import asvid.counter.widget.CounterWidget
import asvid.counter.widget.CounterWidgetProvider
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
        remoteView.setViewVisibility(R.id.incrementButton, View.VISIBLE)
        remoteView.setViewVisibility(R.id.decrementButton, View.VISIBLE)
    }

    override fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews) {
        setNameText(widget.counterItem?.name)
        setValueText(widget.counterItem?.value)
        setStrokeColor(widget.color)
        remoteView.setImageViewBitmap(R.id.imageView, getBitmap())

        appWidgetManager.updateAppWidget(widgetId, remoteView)
        showButtons(remoteView)
        remoteView.setOnClickPendingIntent(R.id.incrementButton,
            getPendingIntent(widgetId, INCREMENT_CLICKED))
        remoteView.setOnClickPendingIntent(R.id.decrementButton,
            getPendingIntent(widgetId, DECREMENT_CLICKED))

        remoteView.setOnClickPendingIntent(widgetId, getPendingIntent(widgetId, ""))
    }

    private fun getPendingIntent(widgetId: Int, action: String): PendingIntent {
        val intent = Intent(context, CounterWidgetProvider::class.java)

        intent.action = CounterWidgetProvider.CLICKED
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        intent.putExtra(BUTTON_ACTION, action)

        val pendingIntent = PendingIntent
            .getBroadcast(context, widgetId, intent, 0)
        return pendingIntent
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