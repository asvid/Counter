package asvid.counter.widget.views

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import asvid.counter.dpToPx
import asvid.counter.widget.CounterWidget
import kotlin.properties.Delegates

val STROKE_SIZE_IN_ID = 5
val INCREMENT_CLICKED = "INCREMENT_CLICKED"
val DECREMENT_CLICKED = "DECREMENT_CLICKED"
val BUTTON_ACTION = "BUTTON_ACTION"

abstract class BaseWidgetView(context: Context, attrs: AttributeSet?, defStyleAttr: Int,
    defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    open val WIDTH_IN_DP = 68
    open val HEIGHT_IN_DP = 68

    var nameView: TextView by Delegates.notNull()
    var valueView: TextView by Delegates.notNull()
    var counterView: FrameLayout by Delegates.notNull()

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs,
        defStyleAttr, 0)

    fun getBitmap(): Bitmap {
        val width = dpToPx(WIDTH_IN_DP)
        val height = dpToPx(HEIGHT_IN_DP)
        this.measure(width, height)
        this.layout(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, ARGB_8888)
        this.draw(Canvas(bitmap))
        return bitmap
    }

    fun setNameText(nameText: String?) {
        this.nameView.text = nameText
    }

    fun setValueText(valueText: Int?) {
        this.valueView.text = valueText?.toString()
    }

    fun setValueText(valueText: String) {
        this.valueView.text = valueText
    }

    fun setStrokeColor(value: Int?) {
        val drawable = counterView.background as GradientDrawable
        drawable.setStroke(dpToPx(STROKE_SIZE_IN_ID), value!!)
    }

    abstract fun update(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews)

    abstract fun setInactive(appWidgetManager: AppWidgetManager, widgetId: Int,
        widget: CounterWidget, remoteView: RemoteViews)
}