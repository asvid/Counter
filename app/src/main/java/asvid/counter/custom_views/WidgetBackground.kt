package asvid.counter.custom_views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import timber.log.Timber

class WidgetBackground(context: Context, attrs: AttributeSet?, defStyleAttr: Int,
    defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    var name: TextView
    var value: TextView
    var counterView: FrameLayout

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs,
        defStyleAttr, 0)

    init {
        inflate(getContext(), R.layout.counter_widget_layout, this)
        this.name = findViewById(R.id.name) as TextView
        this.value = findViewById(R.id.value) as TextView
        this.counterView = findViewById(R.id.counterView) as FrameLayout
    }

    fun setStrokeColor(value: Int?) {
        Timber.d("setting stroke color to: $value")
        val drawable = counterView.background as GradientDrawable
        drawable.setStroke(Di.utils.dpToPx(5), value!!)
    }

    fun setNameText(nameText: String?) {
        this.name.text = nameText
    }

    fun setValueText(valueText: Int?) {
        this.value.text = valueText?.toString()
    }
}