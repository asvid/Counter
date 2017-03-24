package asvid.counter.utils

import android.view.View
import android.view.animation.AlphaAnimation

fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
    val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f,
        1f) else AlphaAnimation(1f, 0f)

    alphaAnimation.duration = duration
    alphaAnimation.fillAfter = true
    v.startAnimation(alphaAnimation)
}