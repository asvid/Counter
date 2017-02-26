package asvid.counter

import android.content.Context
import android.util.DisplayMetrics
import asvid.counter.Di.context

class Utils(context: Context) {

    fun dpToPx(dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}