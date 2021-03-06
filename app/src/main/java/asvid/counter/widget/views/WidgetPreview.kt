package asvid.counter.widget.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import asvid.counter.R
import asvid.counter.dpToPx


class WidgetPreview(val layout: Int, context: Context, attrs: AttributeSet?, defStyleAttr: Int,
    defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


  val SIZE_IN_DP = 72
  val STROKE_SIZE_IN_ID = 5

  var nameView: TextView
  var valueView: TextView
  var counterView: FrameLayout

  constructor(context: Context) : this(R.layout.counter_widget_preview_layout, context, null, 0,
      0)

  constructor(context: Context, attrs: AttributeSet) : this(
      R.layout.counter_widget_preview_layout, context, attrs, 0, 0)

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
      R.layout.counter_widget_preview_layout, context, attrs,
      defStyleAttr, 0)

  init {
    inflate(getContext(), layout, this)
    this.nameView = findViewById(R.id.counterName)
    this.valueView = findViewById(R.id.counterStartValue)
    this.counterView = findViewById(R.id.counterView)
  }

  fun setStrokeColor(value: Int?) {
    val drawable = counterView.background as GradientDrawable
    drawable.setStroke(dpToPx(STROKE_SIZE_IN_ID), value!!)
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

  fun getBitmap(): Bitmap {
    val size = dpToPx(SIZE_IN_DP)
    this.measure(size, size)
    this.layout(0, 0, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    this.draw(Canvas(bitmap))
    return bitmap
  }

  fun getBitmap(sizeFactor: Int): Bitmap {
    val size = dpToPx(SIZE_IN_DP)
    this.measure(size * sizeFactor, size)
    this.layout(0, 0, size * sizeFactor, size)
    val bitmap = Bitmap.createBitmap(size * sizeFactor, size, Bitmap.Config.ARGB_8888)
    this.draw(Canvas(bitmap))
    return bitmap
  }

}