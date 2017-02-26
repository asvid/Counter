package asvid.counter

import android.graphics.Color
import android.util.DisplayMetrics
import asvid.counter.Di.context

fun dpToPx(dp: Int): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun getComplementaryColor(color: Int): Int {
    return Color.argb(Color.alpha(color), 255 - Color.red(color),
        255 - Color.green(color),
        255 - Color.blue(color))
}

fun addAlphaToColor(color: Int, alpha: Int): Int {
    return Color.argb(alpha, Color.red(color),
        Color.green(color),
        Color.blue(color))
}
