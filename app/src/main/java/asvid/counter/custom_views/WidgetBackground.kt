package asvid.counter.custom_views

import android.content.Context
import android.widget.FrameLayout
import timber.log.Timber

class WidgetBackground(context: Context?) : FrameLayout(context) {


    fun setStrokeColor(value: Int) {
        Timber.d("setting stroke color to: $value")
    }
}