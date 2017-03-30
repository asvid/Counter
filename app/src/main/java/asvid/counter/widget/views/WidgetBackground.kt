package asvid.counter.widget.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import asvid.counter.R.layout
import asvid.counter.dpToPx

class WidgetBackground(context: Context, attrs: AttributeSet?, defStyleAttr: Int,
    defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs,
        defStyleAttr, 0)

    init {
        inflate(getContext(), layout.widget_background, this)
    }


    fun getBitmap(widthInDp: Int, heightInDp: Int): Bitmap {
        val width = dpToPx(widthInDp)
        val height = dpToPx(heightInDp)
        this.measure(width, height)
        this.layout(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, ARGB_8888)
        this.draw(Canvas(bitmap))
        return bitmap
    }
}